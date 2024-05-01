package id.ac.ui.cs.advprog.subsmanagementservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_box_id")
    @JsonBackReference //  prevents serialization of parent
    private SubscriptionBox subscriptionBox;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public Item() {
    }

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionBox getSubscriptionBox() {
        return subscriptionBox;
    }

    public void setSubscriptionBox(SubscriptionBox subscriptionBox) {
        this.subscriptionBox = subscriptionBox;
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
}
