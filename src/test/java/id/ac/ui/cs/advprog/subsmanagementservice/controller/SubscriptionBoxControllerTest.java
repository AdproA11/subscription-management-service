package id.ac.ui.cs.advprog.subsmanagementservice.controller;

import id.ac.ui.cs.advprog.subsmanagementservice.model.Subscription;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionDetail;
import id.ac.ui.cs.advprog.subsmanagementservice.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;

class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
// sementara comment dulu ampe udah konek sama service box-item management
/*    @Test
    void getAllSubscriptionBoxesAsync() throws Exception {
        List<SubscriptionBox> subscriptionBoxTests = new ArrayList<>();
        subscriptionBoxTests.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0));
        subscriptionBoxTests.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 20.0));

        CompletableFuture<List<SubscriptionBox>> futureSubscriptionBoxes = CompletableFuture.completedFuture(subscriptionBoxTests);

        when(subscriptionService.getAllBoxesAsync()).thenReturn(futureSubscriptionBoxes);

        CompletableFuture<ResponseEntity<List<SubscriptionBox>>> responseEntityFuture = subscriptionController.getAllSubscriptionBoxesAsync();
        ResponseEntity<List<SubscriptionBox>> responseEntity = responseEntityFuture.get(); // blocking call to get the result

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subscriptionBoxTests, responseEntity.getBody());
    }*/

    @Test
    void getSubscriptionBoxDetails() {
        Long boxId = 1L;
        SubscriptionBox subscriptionBox = new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0);

        when(subscriptionService.findBoxById(boxId)).thenReturn(subscriptionBox);

        ResponseEntity<SubscriptionBox> responseEntity = subscriptionController.getSubscriptionBox(boxId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subscriptionBox, responseEntity.getBody());
    }

    @Test
    void getSubscriptionBoxDetails_ResourceNotFoundException() {
        Long boxId = 1L;

        when(subscriptionService.findBoxById(boxId)).thenThrow(new ResourceNotFoundException("Box not found"));

        assertThrows(ResourceNotFoundException.class, () -> subscriptionController.getSubscriptionBox(boxId));
    }

    @Test
    void subscribeToBox_ShouldFailIfBoxDoesNotExist() {
        Long boxId = 1L;
        doThrow(new ResourceNotFoundException("No subscription box found with ID: " + boxId))
                .when(subscriptionService).subscribeToBox(boxId, "monthly", "1");

        ResponseEntity<Subscription> responseEntity = subscriptionController.subscribe(boxId, Map.of("type", "monthly", "userId", "1"));
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void unsubscribeFromBox_ShouldFailIfBoxDoesNotExist() {
        String subscriptionCode = "non-existent-id";
        when(subscriptionService.unsubscribe(subscriptionCode)).thenReturn(false);

        ResponseEntity<String> responseEntity = subscriptionController.unsubscribe(subscriptionCode);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Unsubscribe failed: Subscription not found", responseEntity.getBody());
    }

    @Test
    public void testGetSubscriptionByStatusIsNoContent() throws Exception {
        // Set up: Mocking subscriptionService behavior for 'Subscribed' status
        when(subscriptionService.getSubscriptionByStatusAsync("Subscribed"))
                .thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        // Execution: Calling the controller method with 'Subscribed' status
        CompletableFuture<ResponseEntity<List<SubscriptionDetail>>> responseFuture = subscriptionController.getSubscriptionByStatus("Subscribed");

        // Wait for the future to complete and get the response entity
        ResponseEntity<List<SubscriptionDetail>> responseEntity = responseFuture.get();

        // Assertion: Asserting the response is HttpStatus.NO_CONTENT and the body is empty
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testGetSubscriptionByStatusIsWithContent() throws Exception {
        // Mocking subscriptionService behavior
        SubscriptionDetail subscription1 = new SubscriptionDetail();
        subscription1.setSubscriptionCode("MTH-ABC123");
        subscription1.setOwnerUsername("user1");
        subscription1.setBoxId(1L);
        subscription1.setType("monthly");
        subscription1.setStatus("Subscribed");

        List<SubscriptionDetail> subscriptions = new ArrayList<>();
        subscriptions.add(subscription1);
        when(subscriptionService.getSubscriptionByStatusAsync("Subscribed"))
                .thenReturn(CompletableFuture.completedFuture(subscriptions));

        // Calling the controller method
        CompletableFuture<ResponseEntity<List<SubscriptionDetail>>> responseFuture = subscriptionController.getSubscriptionByStatus("Subscribed");

        // Wait for the future to complete and get the response entity
        ResponseEntity<List<SubscriptionDetail>> responseEntity = responseFuture.get();

        // Asserting the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subscriptions, responseEntity.getBody());
    }

    @Test
    public void testGetSubscriptionPendingIsNoContent() {
        // Set up: Mocking subscriptionService behavior for 'active' status
        when(subscriptionService.getPendingSubscription()).thenReturn(new ArrayList<>());

        // Execution: Calling the controller method with 'active' status
        ResponseEntity<List<SubscriptionDetail>> responseEntity = subscriptionController.getSubscriptionPending();

        // Assertion: Asserting the response is HttpStatus.NO_CONTENT and the body is empty
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testGetSubscriptionPendingIsWithContent() {
        // Mocking subscriptionService behavior
        SubscriptionDetail subscription1 = new SubscriptionDetail();
        subscription1.setSubscriptionCode("MTH-ABC123");
        subscription1.setOwnerUsername("1");
        subscription1.setBoxId(1L);
        subscription1.setType("monthly");
        subscription1.setStatus("Pending");

        List<SubscriptionDetail> subscriptions = new ArrayList<>();
        subscriptions.add(subscription1);
        when(subscriptionService.getPendingSubscription()).thenReturn(subscriptions);

        // Calling the controller method
        ResponseEntity<List<SubscriptionDetail>> responseEntity = subscriptionController.getSubscriptionPending();

        // Asserting the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subscriptions, responseEntity.getBody());
    }

    @Test
    void accept_subcription_failed() {
        String subscriptionCode = "non-existent-id";
        when(subscriptionService.acceptsubcribed(subscriptionCode)).thenReturn(false);

        ResponseEntity<String> responseEntity = subscriptionController.accept_subscription(subscriptionCode);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Accept Subscription failed: Subscription not found", responseEntity.getBody());
    }

    @Test
    void accept_subcription_success() {
        String subscriptionCode = "MTH-ABC123";
        when(subscriptionService.acceptsubcribed(subscriptionCode)).thenReturn(true);

        ResponseEntity<String> responseEntity = subscriptionController.accept_subscription(subscriptionCode);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully Accept Subscription", responseEntity.getBody());
    }

}
