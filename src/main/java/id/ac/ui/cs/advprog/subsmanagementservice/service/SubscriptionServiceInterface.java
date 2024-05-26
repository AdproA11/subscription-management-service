package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.model.Subscription;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionDetail;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SubscriptionServiceInterface {
    public Subscription subscribeToBox(Long boxId, String type, String ownerUsername);
    public boolean unsubscribe(String subscriptionCode);
    public List<SubscriptionDetail> getUserSubscriptions(String ownerUsername);
    public CompletableFuture<List<SubscriptionDetail>> getSubscriptionByStatusAsync(String status);
    public List<SubscriptionDetail> getPendingSubscription();
    public boolean acceptsubcribed(String subscriptionCode);
}
