package id.ac.ui.cs.advprog.subsmanagementservice.model;

import javax.persistence.*;

@Entity
@Table(name = "subscription_box")
public class SubscriptionBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name= "name",nullable = false)
    private String name;

    @Column(name= "price",nullable = false)
    private double price;

    // tambahin Constructors, getters, and setters
    // Constructors
    public SubscriptionBox() {
    }

    public SubscriptionBox(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
