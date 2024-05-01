package id.ac.ui.cs.advprog.subsmanagementservice.repository;

import id.ac.ui.cs.advprog.subsmanagementservice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
