package id.ac.ui.cs.advprog.subsmanagementservice.repository;

import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionBoxRepository extends JpaRepository<SubscriptionBox, Long> {
    List<SubscriptionBox> findByPriceGreaterThanEqualAndPriceLessThanEqual(Double minPrice, Double maxPrice);
    List<SubscriptionBox> findByNameContaining(String keyword);
}
