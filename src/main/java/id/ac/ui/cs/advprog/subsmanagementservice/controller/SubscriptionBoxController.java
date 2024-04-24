package id.ac.ui.cs.advprog.subsmanagementservice.controller;

import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.service.SubscriptionBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscription-box")
public class SubscriptionBoxController {

    private final SubscriptionBoxService subscriptionService;

    @Autowired
    public SubscriptionBoxController(SubscriptionBoxService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubscriptionBox>> getAllSubscriptionBoxes() {
        List<SubscriptionBox> subscriptionBoxes = subscriptionService.findAllSubscriptionBoxes();
        return ResponseEntity.ok(subscriptionBoxes);
    }

    @GetMapping("/{boxId}")
    public ResponseEntity<SubscriptionBox> getSubscriptionBoxDetails(@PathVariable String boxId) {
        Optional<SubscriptionBox> subscriptionBox = subscriptionService.findSubscriptionBoxById(boxId);
        return ResponseEntity.ok(subscriptionBox.get());
    }
}
