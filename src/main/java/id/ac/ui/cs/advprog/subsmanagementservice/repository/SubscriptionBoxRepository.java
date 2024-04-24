package id.ac.ui.cs.advprog.subsmanagementservice.repository;

import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionBoxRepository extends JpaRepository<SubscriptionBox, String> {
}
