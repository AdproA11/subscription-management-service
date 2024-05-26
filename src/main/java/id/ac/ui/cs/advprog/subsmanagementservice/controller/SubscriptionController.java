package id.ac.ui.cs.advprog.subsmanagementservice.controller;

import id.ac.ui.cs.advprog.subsmanagementservice.model.Subscription;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionDetail;
import id.ac.ui.cs.advprog.subsmanagementservice.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<SubscriptionBox>>> getAllSubscriptionBoxesAsync() {
        return subscriptionService.getAllBoxesAsync()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionBox>> getFilteredSubscriptions(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String keywords) {
        List<SubscriptionBox> boxes = subscriptionService.findAllBoxes(minPrice, maxPrice, keywords);
        return new ResponseEntity<>(boxes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionBox> getSubscriptionBox(@PathVariable Long id) {
        SubscriptionBox box = subscriptionService.findBoxById(id);
        return box != null ? ResponseEntity.ok(box) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Subscription> subscribe(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            String type = requestBody.get("type");
            String userId = requestBody.get("userId");
            Subscription subscription = subscriptionService.subscribeToBox(id, type, userId);
            return ResponseEntity.ok(subscription);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestParam String subscriptionCode) {
        boolean success = subscriptionService.unsubscribe(subscriptionCode);
        return success ? ResponseEntity.ok("Unsubscribed successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unsubscribe failed: Subscription not found");
    }

    @GetMapping("/user-subscriptions")
    public ResponseEntity<List<SubscriptionDetail>> getUserSubscriptions(@RequestParam String ownerUsername) {
        List<SubscriptionDetail> subscriptions = subscriptionService.getUserSubscriptions(ownerUsername);
        return subscriptions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/user-subscriptions-status")
    public CompletableFuture<ResponseEntity<List<SubscriptionDetail>>> getSubscriptionByStatus(@RequestParam String status) {
        return subscriptionService.getSubscriptionByStatusAsync(status)
                .thenApply(subscriptions -> subscriptions.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(subscriptions));
    }
    @GetMapping("/user-subscriptions-pending")
    public ResponseEntity<List<SubscriptionDetail>> getSubscriptionPending() {
        List<SubscriptionDetail> subscriptions = subscriptionService.getPendingSubscription();
        return subscriptions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(subscriptions);
    }
}
