package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.handler.ResourceNotFoundException;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionBoxServiceImpl implements SubscriptionBoxService {

    private final SubscriptionBoxRepository subscriptionBoxRepository;

    public SubscriptionBoxServiceImpl(SubscriptionBoxRepository subscriptionBoxRepository) {
        this.subscriptionBoxRepository = subscriptionBoxRepository;
    }

    @Override
    public List<SubscriptionBox> findAllSubscriptionBoxes() {
        return subscriptionBoxRepository.findAll();
    }

    @Override
    public Optional<SubscriptionBox> findSubscriptionBoxById(String boxId) {
        Optional<SubscriptionBox> subscriptionBox = subscriptionBoxRepository.findById(boxId);
        if (subscriptionBox.isEmpty()) {
            throw new ResourceNotFoundException("Subscription box not found with id: " + boxId);
        }
        return subscriptionBox;
    }
    @Override
    public void subscribeToBox(String boxId) {
        // tdd skeleton
    }

    @Override
    public void unsubscribeFromBox(String boxId) {
        // tdd skeleton
    }

}

