package id.ac.ui.cs.advprog.subsmanagementservice.service;

import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;

import java.util.List;
import java.util.Optional;

public interface SubscriptionBoxService {

    List<SubscriptionBox> findAllSubscriptionBoxes();

    Optional<SubscriptionBox> findSubscriptionBoxById(String boxId);
}
