package id.ac.ui.cs.advprog.subsmanagementservice.model;

public interface SubscriptionType {
    String generateSubscriptionCode();
    String getType();
    double calculateTotal(double basePrice);
}

