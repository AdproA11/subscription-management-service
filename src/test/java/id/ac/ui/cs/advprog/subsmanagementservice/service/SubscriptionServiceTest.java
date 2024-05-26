package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;
import id.ac.ui.cs.advprog.subsmanagementservice.model.*;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionBoxRepository boxRepo;

    @Mock
    private SubscriptionRepository subRepo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void subscribeToBox_Successful() {
        Long boxId = 1L;
        SubscriptionBox box = new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0);

        Subscription newSubscription = new Subscription();
        newSubscription.setSubscriptionCode("MTH-ABC123");
        newSubscription.setOwnerUsername("1");
        newSubscription.setBoxId(boxId);
        newSubscription.setType("monthly");
        newSubscription.setStatus("Pending");

        when(boxRepo.findById(boxId)).thenReturn(Optional.of(box));
        when(subRepo.save(ArgumentMatchers.any(Subscription.class))).thenReturn(newSubscription);

        Subscription result = subscriptionService.subscribeToBox(boxId, "monthly", "1");

        assertEquals("MTH-ABC123", result.getSubscriptionCode());
    }

    @Test
    void unsubscribe_Successful() {
        String subscriptionCode = "MTH-ABC123";
        Subscription subscription = new Subscription();
        subscription.setSubscriptionCode(subscriptionCode);
        subscription.setStatus("Active");

        when(subRepo.findBySubscriptionCode(subscriptionCode)).thenReturn(subscription);

        boolean result = subscriptionService.unsubscribe(subscriptionCode);

        assertEquals(true, result);
    }

    @Test
    void unsubscribe_Failure() {
        String subscriptionCode = "NON_EXISTENT";

        when(subRepo.findBySubscriptionCode(subscriptionCode)).thenReturn(null);

        boolean result = subscriptionService.unsubscribe(subscriptionCode);

        assertEquals(false, result);
    }

    @Test
    public void testGetSubscriptionByStatusIsTrue() throws Exception {
        // Mock repository behavior
        Subscription subscription1 = new Subscription();
        subscription1.setSubscriptionCode("MTH-ABC123");
        subscription1.setOwnerUsername("1");
        subscription1.setBoxId(1L);
        subscription1.setType("monthly");
        subscription1.setStatus("Pending");
        when(subRepo.findByStatus("Pending")).thenReturn(List.of(subscription1));

        SubscriptionBox box = new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0);
        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));

        // Call the asynchronous service method
        CompletableFuture<List<SubscriptionDetail>> resultFuture = subscriptionService.getSubscriptionByStatusAsync("Pending");

        // Wait for the future to complete and get the result
        List<SubscriptionDetail> result = resultFuture.get();

        // Assert the status of the first result
        assertEquals("Pending", result.get(0).getStatus());
    }

    @Test
    public void testGetSubscriptionByStatusIsValid() throws Exception {
        // Mock repository behavior
        Subscription subscription1 = new Subscription();
        subscription1.setSubscriptionCode("MTH-ABC123");
        subscription1.setOwnerUsername("1");
        subscription1.setBoxId(1L);
        subscription1.setType("monthly");
        subscription1.setStatus("Pending");
        when(subRepo.findByStatus("Pending")).thenReturn(List.of(subscription1));

        SubscriptionBox box = new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0);
        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));

        // Call the asynchronous service method
        CompletableFuture<List<SubscriptionDetail>> resultFuture = subscriptionService.getSubscriptionByStatusAsync("Pending");

        // Wait for the future to complete and get the result
        List<SubscriptionDetail> result = resultFuture.get();

        // Assert the result
        assertEquals(1, result.size());
        SubscriptionDetail detail = result.get(0);
        assertEquals("MTH-ABC123", detail.getSubscriptionCode());
        assertEquals("1", detail.getOwnerUsername());
        assertEquals(1L, detail.getBoxId());
        assertEquals("monthly", detail.getType());
        assertEquals("Pending", detail.getStatus());
        assertEquals("Real Madrid Box", detail.getBoxName());
        assertEquals(9.0, detail.getTotal());
    }

    @Test
    void testGetPendingSubscription() {
        // Given
        Subscription subscription1 = new Subscription();
        subscription1.setSubscriptionCode("MTH-ABC123");
        subscription1.setOwnerUsername("1");
        subscription1.setBoxId(1L);
        subscription1.setType("monthly");
        subscription1.setStatus("Pending");

        SubscriptionBox box = new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0);
        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));

        when(subRepo.findByStatus("Pending")).thenReturn(Arrays.asList(subscription1));

        // When
        List<SubscriptionDetail> result = subscriptionService.getPendingSubscription();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("MTH-ABC123", result.get(0).getSubscriptionCode());
        assertEquals("1", result.get(0).getOwnerUsername());
        assertEquals("Real Madrid Box", result.get(0).getBoxName());
        assertEquals(9.0, result.get(0).getTotal()); // assuming a 10% discount for monthly subscriptions

        // Verify interactions
        verify(subRepo, times(1)).findByStatus("Pending");
        verify(boxRepo, times(1)).findById(1L);
    }

    @Test
    void acceptSubscriptionSuccessful() {
        String subscriptionCode = "MTH-ABC123";
        Subscription subscription = new Subscription();
        subscription.setSubscriptionCode(subscriptionCode);
        subscription.setStatus("Pending");

        when(subRepo.findBySubscriptionCode(subscriptionCode)).thenReturn(subscription);

        boolean result = subscriptionService.acceptsubcribed(subscriptionCode);

        assertEquals(true, result);
    }

    @Test
    void acceptSubscriptionFailure() {
        String subscriptionCode = "NON_EXISTENT";

        when(subRepo.findBySubscriptionCode(subscriptionCode)).thenReturn(null);

        boolean result = subscriptionService.acceptsubcribed(subscriptionCode);

        assertEquals(false, result);
    }
    @Test
    public void testCreateSubscriptionType() {
        String baseCode = UUID.randomUUID().toString();

        SubscriptionType monthly = subscriptionService.createSubscriptionType("monthly", baseCode);
        assertInstanceOf(MonthlySubscription.class, monthly);

        SubscriptionType quarterly = subscriptionService.createSubscriptionType("quarterly", baseCode);
        assertInstanceOf(QuarterlySubscription.class, quarterly);

        SubscriptionType semiAnnual = subscriptionService.createSubscriptionType("semi-annual", baseCode);
        assertInstanceOf(SemiAnnualSubscription.class, semiAnnual);

        SubscriptionType basic = subscriptionService.createSubscriptionType("other", baseCode);
        assertInstanceOf(BasicSubscription.class, basic);
    }

    @Test
    public void testUpdateSubscriptionStatus() {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionCode("code");
        subscription.setStatus("Active");

        when(subRepo.findBySubscriptionCode("code")).thenReturn(subscription);

        boolean result = subscriptionService.updateSubscriptionStatus("code", "Cancelled");
        assertTrue(result);
        assertEquals("Cancelled", subscription.getStatus());
        verify(subRepo, times(1)).save(subscription);

        when(subRepo.findBySubscriptionCode("invalid_code")).thenReturn(null);
        result = subscriptionService.updateSubscriptionStatus("invalid_code", "Cancelled");
        assertFalse(result);
    }

    @Test
    public void testMapSubscriptionsToDetails() {
        Subscription subscription1 = new Subscription();
        subscription1.setBoxId(1L);
        subscription1.setSubscriptionCode("code1");
        subscription1.setOwnerUsername("user1");
        subscription1.setType("monthly");
        subscription1.setStatus("Active");

        Subscription subscription2 = new Subscription();
        subscription2.setBoxId(2L);
        subscription2.setSubscriptionCode("code2");
        subscription2.setOwnerUsername("user2");
        subscription2.setType("quarterly");
        subscription2.setStatus("Pending");

        List<Subscription> subscriptions = Arrays.asList(subscription1, subscription2);

        SubscriptionBox box1 = new SubscriptionBox();
        box1.setId(1L);
        box1.setName("Box1");
        box1.setPrice(100.0);

        SubscriptionBox box2 = new SubscriptionBox();
        box2.setId(2L);
        box2.setName("Box2");
        box2.setPrice(200.0);

        when(boxRepo.findById(1L)).thenReturn(Optional.of(box1));
        when(boxRepo.findById(2L)).thenReturn(Optional.of(box2));

        List<SubscriptionDetail> details = subscriptionService.mapSubscriptionsToDetails(subscriptions);

        assertEquals(2, details.size());

        SubscriptionDetail detail1 = details.get(0);
        assertEquals("code1", detail1.getSubscriptionCode());
        assertEquals("user1", detail1.getOwnerUsername());
        assertEquals("Box1", detail1.getBoxName());
        assertEquals("monthly", detail1.getType());
        assertEquals("Active", detail1.getStatus());

        SubscriptionDetail detail2 = details.get(1);
        assertEquals("code2", detail2.getSubscriptionCode());
        assertEquals("user2", detail2.getOwnerUsername());
        assertEquals("Box2", detail2.getBoxName());
        assertEquals("quarterly", detail2.getType());
        assertEquals("Pending", detail2.getStatus());
    }

    @Test
    public void testMapSubscriptionToDetail() {
        Subscription subscription = new Subscription();
        subscription.setBoxId(1L);
        subscription.setSubscriptionCode("code1");
        subscription.setOwnerUsername("user1");
        subscription.setType("monthly");
        subscription.setStatus("Active");

        SubscriptionBox box = new SubscriptionBox();
        box.setId(1L);
        box.setName("Box1");
        box.setPrice(100.0);

        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));

        SubscriptionDetail detail = subscriptionService.mapSubscriptionToDetail(subscription);

        assertEquals("code1", detail.getSubscriptionCode());
        assertEquals("user1", detail.getOwnerUsername());
        assertEquals("Box1", detail.getBoxName());
        assertEquals("monthly", detail.getType());
        assertEquals("Active", detail.getStatus());
        assertEquals(90.0, detail.getTotal(), 0.01); // Assuming no discounts applied for simplicity
    }
}