package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SubscriptionBoxServiceInterface {
    public CompletableFuture<List<SubscriptionBox>> getAllBoxesAsync();
    public List<SubscriptionBox> findAllBoxes(Double minPrice, Double maxPrice, String keywords);
    public SubscriptionBox findBoxById(Long id);
}
