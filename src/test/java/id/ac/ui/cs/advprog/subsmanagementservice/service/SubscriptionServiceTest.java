package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;
import id.ac.ui.cs.advprog.subsmanagementservice.model.Subscription;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionDetail;
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

    // sementara comment dulu ampe udah konek sama service box-item management
/*    @Test
    void getAllBoxesAsync() throws Exception {
        List<SubscriptionBox> subscriptionBoxes = new ArrayList<>();
        subscriptionBoxes.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0));
        subscriptionBoxes.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 20.0));

        String url = "http://localhost:8081/api/box/all";
        ResponseEntity<List<SubscriptionBox>> responseEntity = ResponseEntity.ok(subscriptionBoxes);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseEntity);

        CompletableFuture<List<SubscriptionBox>> futureResult = subscriptionService.getAllBoxesAsync();
        List<SubscriptionBox> result = futureResult.get(); // blocking call to get the result for consistency


        assertEquals(subscriptionBoxes.size(), result.size());
        assertEquals(subscriptionBoxes, result);

        verify(boxRepo, times(1)).saveAll(subscriptionBoxes);
    }*/

    @Test
    void findBoxById_ResourceNotFoundException() {
        Long boxId = 1L;

        when(boxRepo.findById(boxId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subscriptionService.findBoxById(boxId));
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
}
