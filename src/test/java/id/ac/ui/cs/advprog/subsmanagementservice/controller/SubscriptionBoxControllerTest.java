package id.ac.ui.cs.advprog.subsmanagementservice.controller;

import id.ac.ui.cs.advprog.subsmanagementservice.model.Subscription;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void getAllSubscriptionBoxes() {
        List<SubscriptionBox> subscriptionBoxTests = new ArrayList<>();
        subscriptionBoxTests.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0));
        subscriptionBoxTests.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 20.0));

        when(subscriptionService.getAllBoxes()).thenReturn(subscriptionBoxTests);

        ResponseEntity<List<SubscriptionBox>> responseEntity = subscriptionController.getAllSubscriptionBoxes();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subscriptionBoxTests, responseEntity.getBody());
    }

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
}
