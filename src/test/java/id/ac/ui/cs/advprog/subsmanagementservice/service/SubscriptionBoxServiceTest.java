package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionBoxServiceTest {

    @Mock
    private SubscriptionBoxRepository boxRepo;

    @Mock
    private SubscriptionRepository subRepo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SubscriptionBoxService subscriptionBoxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBoxesAsync() throws Exception {
        List<SubscriptionBox> subscriptionBoxes = new ArrayList<>();
        subscriptionBoxes.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0));
        subscriptionBoxes.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 20.0));

        String url = "http://34.126.126.9/api/subscription-box/all";
        ResponseEntity<List<SubscriptionBox>> responseEntity = ResponseEntity.ok(subscriptionBoxes);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseEntity);

        CompletableFuture<List<SubscriptionBox>> futureResult = subscriptionBoxService.getAllBoxesAsync();
        List<SubscriptionBox> result = futureResult.get(); // blocking call to get the result for consistency

        assertEquals(subscriptionBoxes.size(), result.size());
        assertEquals(subscriptionBoxes, result);

        verify(boxRepo, times(1)).saveAll(subscriptionBoxes);
    }

    @Test
    void findBoxById_ResourceNotFoundException() {
        Long boxId = 1L;

        when(boxRepo.findById(boxId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subscriptionBoxService.findBoxById(boxId));
    }

    @Test
    void findAllBoxes_WithMinMaxPrice() {
        Double minPrice = 10.0;
        Double maxPrice = 20.0;
        List<SubscriptionBox> subscriptionBoxes = new ArrayList<>();
        subscriptionBoxes.add(new SubscriptionBox("Box1", "Description1", 15.0));

        when(boxRepo.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice))
                .thenReturn(subscriptionBoxes);

        List<SubscriptionBox> result = subscriptionBoxService.findAllBoxes(minPrice, maxPrice, null);

        assertEquals(subscriptionBoxes.size(), result.size());
        assertEquals(subscriptionBoxes, result);
    }

    @Test
    void findAllBoxes_WithKeywords() {
        String keywords = "Real Madrid";
        List<SubscriptionBox> subscriptionBoxes = new ArrayList<>();
        subscriptionBoxes.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 15.0));

        when(boxRepo.findByNameContaining(keywords)).thenReturn(subscriptionBoxes);

        List<SubscriptionBox> result = subscriptionBoxService.findAllBoxes(null, null, keywords);

        assertEquals(subscriptionBoxes.size(), result.size());
        assertEquals(subscriptionBoxes, result);
    }

    @Test
    void findAllBoxes_WithMinMaxPriceAndKeywords() {
        Double minPrice = 10.0;
        Double maxPrice = 20.0;
        String keywords = "Real Madrid";
        List<SubscriptionBox> subscriptionBoxes = new ArrayList<>();
        subscriptionBoxes.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 15.0));

        when(boxRepo.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice))
                .thenReturn(subscriptionBoxes);

        List<SubscriptionBox> result = subscriptionBoxService.findAllBoxes(minPrice, maxPrice, keywords);

        assertEquals(subscriptionBoxes.size(), result.size());
        assertTrue(result.stream().allMatch(box -> box.getName().contains(keywords)));
    }

    @Test
    void findAllBoxes_NoParameters() {
        List<SubscriptionBox> subscriptionBoxes = new ArrayList<>();
        subscriptionBoxes.add(new SubscriptionBox("Box1", "Description1", 15.0));
        subscriptionBoxes.add(new SubscriptionBox("Box2", "Description2", 25.0));

        when(boxRepo.findAll()).thenReturn(subscriptionBoxes);

        List<SubscriptionBox> result = subscriptionBoxService.findAllBoxes(null, null, null);

        assertEquals(subscriptionBoxes.size(), result.size());
        assertEquals(subscriptionBoxes, result);
    }
}
