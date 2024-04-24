package id.ac.ui.cs.advprog.subsmanagementservice.controller;

import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.service.SubscriptionBoxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SubscriptionBoxControllerTest {

    @Mock
    private SubscriptionBoxService subscriptionBoxService;

    @InjectMocks
    private SubscriptionBoxController subscriptionBoxController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSubscriptionBoxes() {
        List<SubscriptionBox> subscriptionBoxTests = new ArrayList<>();
        subscriptionBoxTests.add(new SubscriptionBox("Box1", 10.0));
        subscriptionBoxTests.add(new SubscriptionBox("Box2", 20.0));

        when(subscriptionBoxService.findAllSubscriptionBoxes()).thenReturn(subscriptionBoxTests);

        ResponseEntity<List<SubscriptionBox>> responseEntity = subscriptionBoxController.getAllSubscriptionBoxes();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subscriptionBoxTests, responseEntity.getBody());
    }

    @Test
    void getSubscriptionBoxDetails() {
        String boxId = "1";
        SubscriptionBox subscriptionBox = new SubscriptionBox("Box1", 10.0);

        when(subscriptionBoxService.findSubscriptionBoxById(boxId)).thenReturn(Optional.of(subscriptionBox));

        ResponseEntity<SubscriptionBox> responseEntity = subscriptionBoxController.getSubscriptionBoxDetails(boxId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(subscriptionBox, responseEntity.getBody());
    }
}
