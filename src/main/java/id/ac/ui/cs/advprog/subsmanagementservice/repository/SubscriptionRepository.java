package id.ac.ui.cs.advprog.subsmanagementservice.repository;

import id.ac.ui.cs.advprog.subsmanagementservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findBySubscriptionCode(String subscriptionCode);
    List<Subscription> findByOwnerUsername(String ownerUsername);
    List<Subscription> findByStatus(String status);
}
