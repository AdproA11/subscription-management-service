package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionBoxServiceImplTest {

    @Mock
    private SubscriptionBoxRepository subscriptionBoxRepository;

    @InjectMocks
    private SubscriptionBoxServiceImpl subscriptionBoxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllSubscriptionBoxes() {
        List<SubscriptionBox> subscriptionBoxes = new ArrayList<>();
        subscriptionBoxes.add(new SubscriptionBox("Box1", 10.0));
        subscriptionBoxes.add(new SubscriptionBox("Box2", 20.0));

        when(subscriptionBoxRepository.findAll()).thenReturn(subscriptionBoxes);

        List<SubscriptionBox> result = subscriptionBoxService.findAllSubscriptionBoxes();

        assertEquals(subscriptionBoxes.size(), result.size());
        assertEquals(subscriptionBoxes, result);
    }

    @Test
    void findSubscriptionBoxById() {
        String boxId = "1";
        SubscriptionBox subscriptionBox = new SubscriptionBox("Box1", 10.0);

        when(subscriptionBoxRepository.findById(boxId)).thenReturn(Optional.of(subscriptionBox));

        Optional<SubscriptionBox> result = subscriptionBoxService.findSubscriptionBoxById(boxId);

        assertTrue(result.isPresent());
        assertEquals(subscriptionBox, result.get());
    }

    @Test
    void findSubscriptionBoxById_ResourceNotFoundException() {
        String boxId = "1";

        when(subscriptionBoxRepository.findById(boxId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> subscriptionBoxService.findSubscriptionBoxById(boxId));
    }
}
