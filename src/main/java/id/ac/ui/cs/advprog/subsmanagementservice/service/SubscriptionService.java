package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;
import id.ac.ui.cs.advprog.subsmanagementservice.model.*;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionRepository;
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
public class SubscriptionService {
    @Autowired
    private SubscriptionBoxRepository boxRepo;
    @Autowired
    private SubscriptionRepository subRepo;
    @Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<List<SubscriptionBox>> getAllBoxesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            String url = "http://localhost:8081/api/box/all";
            ResponseEntity<List<SubscriptionBox>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            List<SubscriptionBox> boxes = response.getBody();
            if (boxes != null) {
                boxRepo.saveAll(boxes);
            }
            return boxes;
        });
    }

    public List<SubscriptionBox> findAllBoxes(Double minPrice, Double maxPrice, String keywords) {
        if (minPrice != null && maxPrice != null && keywords != null) {
            return boxRepo.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice)
                    .stream()
                    .filter(box -> box.getName().contains(keywords))
                    .collect(Collectors.toList());
        } else if (minPrice != null && maxPrice != null) {
            return boxRepo.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice);
        } else if (keywords != null) {
            return boxRepo.findByNameContaining(keywords);
        } else {
            return boxRepo.findAll();
        }
    }


    public SubscriptionBox findBoxById(Long id) throws ResourceNotFoundException {
        return boxRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subscription box not found with id: " + id));
    }

    public Subscription subscribeToBox(Long boxId, String type, String ownerUsername) {
        SubscriptionType subscription = new BasicSubscription(UUID.randomUUID().toString());

        switch (type) {
            case "monthly":
                subscription = new MonthlySubscription(subscription);
                type = "monthly";
                break;
            case "quarterly":
                subscription = new QuarterlySubscription(subscription);
                type = "quarterly";
                break;
            case "semi-annual":
                subscription = new SemiAnnualSubscription(subscription);
                type = "semi-annual";
                break;
        }

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
        Optional<Subscription> subscription = Optional.ofNullable(subRepo.findBySubscriptionCode(subscriptionCode));
        if (subscription.isPresent()) {
            Subscription sub = subscription.get();
            sub.setStatus("Cancelled");
            subRepo.save(sub);
            return true;
        }
        return false;
    }

    public List<SubscriptionDetail> getUserSubscriptions(String ownerUsername) {
        List<Subscription> subscriptions = subRepo.findByOwnerUsername(ownerUsername);
        return subscriptions.stream().map(subscription -> {
            SubscriptionBox box = boxRepo.findById(subscription.getBoxId()).orElseThrow();
            SubscriptionDetail detail = new SubscriptionDetail();
            detail.setId(subscription.getId());
            detail.setSubscriptionCode(subscription.getSubscriptionCode());
            detail.setOwnerUsername(subscription.getOwnerUsername());
            detail.setBoxId(subscription.getBoxId());
            detail.setBoxName(box.getName());
            detail.setType(subscription.getType());
            detail.setStatus(subscription.getStatus());

            // Apply diskon dari Tpe subscription
            SubscriptionType subscriptionType;
            switch (subscription.getType().toLowerCase()) {
                case "monthly":
                    subscriptionType = new MonthlySubscription(new BasicSubscription(subscription.getSubscriptionCode()));
                    break;
                case "quarterly":
                    subscriptionType = new QuarterlySubscription(new BasicSubscription(subscription.getSubscriptionCode()));
                    break;
                case "semi-annual":
                    subscriptionType = new SemiAnnualSubscription(new BasicSubscription(subscription.getSubscriptionCode()));
                    break;
                default:
                    subscriptionType = new BasicSubscription(subscription.getSubscriptionCode());
            }
            detail.setTotal(subscriptionType.calculateTotal(box.getPrice()));

            return detail;

        }).collect(Collectors.toList());
    }
    @Async
    public CompletableFuture<List<SubscriptionDetail>> getSubscriptionByStatusAsync(String status) {
        return CompletableFuture.supplyAsync(() -> subRepo.findByStatus(status))
                .thenCompose(subscriptions -> {
                    List<CompletableFuture<SubscriptionDetail>> futures = subscriptions.stream()
                            .map(subscription -> CompletableFuture.supplyAsync(() -> {
                                SubscriptionBox box = boxRepo.findById(subscription.getBoxId())
                                        .orElseThrow();
                                SubscriptionDetail detail = new SubscriptionDetail();
                                detail.setId(subscription.getId());
                                detail.setSubscriptionCode(subscription.getSubscriptionCode());
                                detail.setOwnerUsername(subscription.getOwnerUsername());
                                detail.setBoxId(subscription.getBoxId());
                                detail.setBoxName(box.getName());
                                detail.setType(subscription.getType());
                                detail.setStatus(subscription.getStatus());

                                // Apply diskon dari Tpe subscription
                                SubscriptionType subscriptionType;
                                switch (subscription.getType().toLowerCase()) {
                                    case "monthly":
                                        subscriptionType = new MonthlySubscription(new BasicSubscription(subscription.getSubscriptionCode()));
                                        break;
                                    case "quarterly":
                                        subscriptionType = new QuarterlySubscription(new BasicSubscription(subscription.getSubscriptionCode()));
                                        break;
                                    case "semi-annual":
                                        subscriptionType = new SemiAnnualSubscription(new BasicSubscription(subscription.getSubscriptionCode()));
                                        break;
                                    default:
                                        subscriptionType = new BasicSubscription(subscription.getSubscriptionCode());
                                }
                                detail.setTotal(subscriptionType.calculateTotal(box.getPrice()));

                                return detail;
                            })).collect(Collectors.toList());

                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                            .thenApply(v -> futures.stream()
                                    .map(CompletableFuture::join)
                                    .collect(Collectors.toList()));
                });
    }
    public List<SubscriptionDetail> getPendingSubscription() {
        List<Subscription> subscriptions = subRepo.findByStatus("Pending");
        return subscriptions.stream().map(subscription -> {
            SubscriptionBox box = boxRepo.findById(subscription.getBoxId()).orElseThrow();
            SubscriptionDetail detail = new SubscriptionDetail();
            detail.setId(subscription.getId());
            detail.setSubscriptionCode(subscription.getSubscriptionCode());
            detail.setOwnerUsername(subscription.getOwnerUsername());
            detail.setBoxId(subscription.getBoxId());
            detail.setBoxName(box.getName());
            detail.setType(subscription.getType());
            detail.setStatus(subscription.getStatus());

            // Apply diskon dari Tpe subscription
            SubscriptionType subscriptionType;
            switch (subscription.getType().toLowerCase()) {
                case "monthly":
                    subscriptionType = new MonthlySubscription(new BasicSubscription(subscription.getSubscriptionCode()));
                    break;
                case "quarterly":
                    subscriptionType = new QuarterlySubscription(new BasicSubscription(subscription.getSubscriptionCode()));
                    break;
                case "semi-annual":
                    subscriptionType = new SemiAnnualSubscription(new BasicSubscription(subscription.getSubscriptionCode()));
                    break;
                default:
                    subscriptionType = new BasicSubscription(subscription.getSubscriptionCode());
            }
            detail.setTotal(subscriptionType.calculateTotal(box.getPrice()));

            return detail;

        }).collect(Collectors.toList());
    }

    public boolean acceptsubcribed(String subscriptionCode) {
        Optional<Subscription> subscription = Optional.ofNullable(subRepo.findBySubscriptionCode(subscriptionCode));
        if (subscription.isPresent()) {
            Subscription sub = subscription.get();
            sub.setStatus("Subscribed");
            subRepo.save(sub);
            return true;
        }
        return false;
    }
}
