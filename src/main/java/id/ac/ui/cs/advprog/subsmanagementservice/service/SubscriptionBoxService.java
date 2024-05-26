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
public class SubscriptionBoxService implements SubscriptionBoxServiceInterface {
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
}
