package id.ac.ui.cs.advprog.subsmanagementservice.model;

public class BasicSubscription implements Subscription {
    private String id;
    private double price;

    public BasicSubscription(String id, double price) {
        this.id = id;
        this.price = price;
    }
    @Override
    public String getId() {
        return id;
    }
    @Override
    public String getDescription() {
        return "";
    }
    @Override
    public double getPrice() {
        return price;
    }
}