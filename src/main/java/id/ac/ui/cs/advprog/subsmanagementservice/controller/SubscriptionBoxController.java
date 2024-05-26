package id.ac.ui.cs.advprog.subsmanagementservice.controller;

import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.service.SubscriptionBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/subscription-box")
public class SubscriptionBoxController {
    @Autowired
    private SubscriptionBoxService subscriptionBoxService;

    @GetMapping("/all")
    public ResponseEntity<List<SubscriptionBox>> getAllSubscriptionBoxes() {
        List<SubscriptionBox> boxes = subscriptionBoxService.getAllBoxes();
        return ResponseEntity.ok(boxes);
    }

    @GetMapping("/all-async")
    public CompletableFuture<ResponseEntity<List<SubscriptionBox>>> getAllSubscriptionBoxesAsync() {
        return subscriptionBoxService.getAllBoxesAsync()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionBox>> getFilteredSubscriptions(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String keywords) {
        List<SubscriptionBox> boxes = subscriptionBoxService.findAllBoxes(minPrice, maxPrice, keywords);
        return new ResponseEntity<>(boxes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionBox> getSubscriptionBox(@PathVariable Long id) {
        SubscriptionBox box = subscriptionBoxService.findBoxById(id);
        return box != null ? ResponseEntity.ok(box) : ResponseEntity.notFound().build();
    }
}
