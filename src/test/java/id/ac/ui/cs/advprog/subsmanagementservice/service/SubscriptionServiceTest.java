package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;
import id.ac.ui.cs.advprog.subsmanagementservice.model.Subscription;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionBoxRepository boxRepo;

    @Mock
    private SubscriptionRepository subRepo;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllBoxes() {
        List<SubscriptionBox> subscriptionBoxes = new ArrayList<>();
        subscriptionBoxes.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 10.0));
        subscriptionBoxes.add(new SubscriptionBox("Real Madrid Box", "Real Madrid Sub Box", 20.0));

        when(boxRepo.findAll()).thenReturn(subscriptionBoxes);

        List<SubscriptionBox> result = subscriptionService.getAllBoxes();

        assertEquals(subscriptionBoxes.size(), result.size());
        assertEquals(subscriptionBoxes, result);
    }

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
}
