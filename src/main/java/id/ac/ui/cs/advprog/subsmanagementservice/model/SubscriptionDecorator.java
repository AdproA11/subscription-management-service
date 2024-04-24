package id.ac.ui.cs.advprog.subsmanagementservice.model;

public abstract class SubscriptionDecorator implements Subscription {
    protected Subscription wrappedSubscription;
    public SubscriptionDecorator(Subscription subscription) {
        this.wrappedSubscription = subscription;
    }
    @Override
    public String getId() {
        return wrappedSubscription.getId();
    }
    @Override
    public String getDescription() {
        return wrappedSubscription.getDescription();
    }
    @Override
    public double getPrice() {
        return wrappedSubscription.getPrice();
    }
}
