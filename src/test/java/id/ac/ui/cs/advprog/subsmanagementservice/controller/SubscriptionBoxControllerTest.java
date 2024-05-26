package id.ac.ui.cs.advprog.subsmanagementservice.controller;

import id.ac.ui.cs.advprog.subsmanagementservice.model.Subscription;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionDetail;
import id.ac.ui.cs.advprog.subsmanagementservice.service.SubscriptionBoxService;
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

class SubscriptionBoxControllerTest {

    @Mock
    private SubscriptionBoxService subscriptionBoxService;

    @InjectMocks
    private SubscriptionBoxController subscriptionBoxController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
// sementara comment dulu ampe udah konek sama service box-item management
   @Test
    void getAllSubscriptionBoxesAsync() throws Exception {
        List<SubscriptionBox> subscriptionBoxTests = new ArrayList<>();
        subscriptionBoxTests.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0));
        subscriptionBoxTests.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 20.0));

        CompletableFuture<List<SubscriptionBox>> futureSubscriptionBoxes = CompletableFuture.completedFuture(subscriptionBoxTests);

        when(subscriptionBoxService.getAllBoxesAsync()).thenReturn(futureSubscriptionBoxes);

        CompletableFuture<ResponseEntity<List<SubscriptionBox>>> responseEntityFuture = subscriptionBoxController.getAllSubscriptionBoxesAsync();
        ResponseEntity<List<SubscriptionBox>> responseEntity = responseEntityFuture.get(); // blocking call to get the result

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subscriptionBoxTests, responseEntity.getBody());
    }

    @Test
    void getSubscriptionBoxDetails() {
        Long boxId = 1L;
        SubscriptionBox subscriptionBox = new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0);

        when(subscriptionBoxService.findBoxById(boxId)).thenReturn(subscriptionBox);

        ResponseEntity<SubscriptionBox> responseEntity = subscriptionBoxController.getSubscriptionBox(boxId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subscriptionBox, responseEntity.getBody());
    }

    @Test
    void getSubscriptionBoxDetails_ResourceNotFoundException() {
        Long boxId = 1L;

        when(subscriptionBoxService.findBoxById(boxId)).thenThrow(new ResourceNotFoundException("Box not found"));

        assertThrows(ResourceNotFoundException.class, () -> subscriptionBoxController.getSubscriptionBox(boxId));
    }

}
