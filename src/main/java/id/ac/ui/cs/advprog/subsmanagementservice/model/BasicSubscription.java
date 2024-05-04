package id.ac.ui.cs.advprog.subsmanagementservice.model;

public class BasicSubscription implements SubscriptionType {
    private String subscriptionCode;

    public BasicSubscription(String subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
    }
    @Override
    public String generateSubscriptionCode() {
        return subscriptionCode;
    }
    @Override
    public String getType() {
        return "Basic";
    }
    @Override
    public double calculateTotal(double basePrice) {
        return basePrice;
    }
}

