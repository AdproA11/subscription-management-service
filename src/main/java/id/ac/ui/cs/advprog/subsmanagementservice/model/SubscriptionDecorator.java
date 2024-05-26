package id.ac.ui.cs.advprog.subsmanagementservice.model;

public abstract class SubscriptionDecorator implements SubscriptionType {
    protected SubscriptionType subscription;

    public SubscriptionDecorator(SubscriptionType subscription) {
        this.subscription = subscription;
    }

    @Override
    public String generateSubscriptionCode() {
        return subscription.generateSubscriptionCode();
    }

    @Override
    public String getType() {
        return subscription.getType();
    }

    @Override
    public double calculateTotal(double basePrice) {
        return subscription.calculateTotal(basePrice);
    }
}
