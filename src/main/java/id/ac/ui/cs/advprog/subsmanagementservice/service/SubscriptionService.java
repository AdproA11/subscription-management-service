package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;
import id.ac.ui.cs.advprog.subsmanagementservice.model.*;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionBoxRepository boxRepo;
    @Autowired
    private SubscriptionRepository subRepo;

    public List<SubscriptionBox> getAllBoxes() {
        return boxRepo.findAll();
    }

    public List<SubscriptionBox> findAllBoxes(Double minPrice, Double maxPrice, String keywords) {
        if (minPrice != null && maxPrice != null) {
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

    public Subscription subscribeToBox(Long boxId, String type, Long userId) {
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
        newSubscription.setUserId(userId);
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
    public List<SubscriptionDetail> getUserSubscriptions(Long userId) {
        List<Subscription> subscriptions = subRepo.findByUserId(userId);
        return subscriptions.stream().map(subscription -> {
            SubscriptionBox box = boxRepo.findById(subscription.getBoxId()).orElseThrow();
            SubscriptionDetail detail = new SubscriptionDetail();
            detail.setId(subscription.getId());
            detail.setSubscriptionCode(subscription.getSubscriptionCode());
            detail.setUserId(subscription.getUserId());
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
}
