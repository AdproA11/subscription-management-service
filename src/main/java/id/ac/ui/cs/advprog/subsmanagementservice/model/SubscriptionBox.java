package id.ac.ui.cs.advprog.subsmanagementservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subscription_boxes")
public class SubscriptionBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "subscriptionBox", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference // manages the serialization of child references
    private Set<Item> items = new HashSet<>();

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    // Constructor for convenience
    public SubscriptionBox(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // Default constructor is necessary for JPA
    public SubscriptionBox() {
    }

    // Standard getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
