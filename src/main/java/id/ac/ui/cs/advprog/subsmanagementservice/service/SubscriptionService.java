package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;
import id.ac.ui.cs.advprog.subsmanagementservice.model.*;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class SubscriptionService implements SubscriptionServiceInterface {
    @Autowired
    private SubscriptionBoxRepository boxRepo;
    @Autowired
    private SubscriptionRepository subRepo;
    @Autowired
    private RestTemplate restTemplate;

    public Subscription subscribeToBox(Long boxId, String type, String ownerUsername) {
        SubscriptionType subscription = createSubscriptionType(type, UUID.randomUUID().toString());

        String subscriptionCode = subscription.generateSubscriptionCode();

        Subscription newSubscription = new Subscription();
        newSubscription.setSubscriptionCode(subscriptionCode);
        newSubscription.setOwnerUsername(ownerUsername);
        newSubscription.setBoxId(boxId);
        newSubscription.setType(type);
        newSubscription.setStatus("Pending");

        SubscriptionBox box = boxRepo.findById(boxId).orElseThrow(() -> new RuntimeException("Box not found"));
        boxRepo.save(box);

        return subRepo.save(newSubscription);
    }

    public boolean unsubscribe(String subscriptionCode) {
        return updateSubscriptionStatus(subscriptionCode, "Cancelled");
    }

    public List<SubscriptionDetail> getUserSubscriptions(String ownerUsername) {
        List<Subscription> subscriptions = subRepo.findByOwnerUsername(ownerUsername);
        return mapSubscriptionsToDetails(subscriptions);
    }

    @Async
    public CompletableFuture<List<SubscriptionDetail>> getSubscriptionByStatusAsync(String status) {
        return CompletableFuture.supplyAsync(() -> subRepo.findByStatus(status))
                .thenCompose(subscriptions -> {
                    List<CompletableFuture<SubscriptionDetail>> futures = subscriptions.stream()
                            .map(subscription -> CompletableFuture.supplyAsync(() -> mapSubscriptionToDetail(subscription)))
                            .collect(Collectors.toList());

                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                            .thenApply(v -> futures.stream()
                                    .map(CompletableFuture::join)
                                    .collect(Collectors.toList()));
                });
    }

    public List<SubscriptionDetail> getPendingSubscription() {
        List<Subscription> subscriptions = subRepo.findByStatus("Pending");
        return mapSubscriptionsToDetails(subscriptions);
    }

    public boolean acceptsubcribed(String subscriptionCode) {
        return updateSubscriptionStatus(subscriptionCode, "Subscribed");
    }

    public SubscriptionType createSubscriptionType(String type, String baseCode) {
        SubscriptionType subscription = new BasicSubscription(baseCode);
        switch (type.toLowerCase()) {
            case "monthly":
                subscription = new MonthlySubscription(subscription);
                break;
            case "quarterly":
                subscription = new QuarterlySubscription(subscription);
                break;
            case "semi-annual":
                subscription = new SemiAnnualSubscription(subscription);
                break;
        }
        return subscription;
    }

    public boolean updateSubscriptionStatus(String subscriptionCode, String status) {
        Optional<Subscription> subscription = Optional.ofNullable(subRepo.findBySubscriptionCode(subscriptionCode));
        if (subscription.isPresent()) {
            Subscription sub = subscription.get();
            sub.setStatus(status);
            subRepo.save(sub);
            return true;
        }
        return false;
    }

    public List<SubscriptionDetail> mapSubscriptionsToDetails(List<Subscription> subscriptions) {
        return subscriptions.stream().map(this::mapSubscriptionToDetail).collect(Collectors.toList());
    }

    public SubscriptionDetail mapSubscriptionToDetail(Subscription subscription) {
        SubscriptionBox box = boxRepo.findById(subscription.getBoxId()).orElseThrow();
        SubscriptionDetail detail = new SubscriptionDetail();
        detail.setId(subscription.getId());
        detail.setSubscriptionCode(subscription.getSubscriptionCode());
        detail.setOwnerUsername(subscription.getOwnerUsername());
        detail.setBoxId(subscription.getBoxId());
        detail.setBoxName(box.getName());
        detail.setType(subscription.getType());
        detail.setStatus(subscription.getStatus());

        SubscriptionType subscriptionType = createSubscriptionType(subscription.getType(), subscription.getSubscriptionCode());
        detail.setTotal(subscriptionType.calculateTotal(box.getPrice()));

        return detail;
    }
}
