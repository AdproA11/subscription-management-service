package id.ac.ui.cs.advprog.subsmanagementservice.model;

public class QuarterlySubscriptionDecorator extends SubscriptionDecorator {
    public QuarterlySubscriptionDecorator(Subscription subscription) {
        super(subscription);
    }
    @Override
    public String getDescription() {
        return super.getDescription() + "Quarterly Subscription";
    }
    @Override
    public double getPrice() {
        return super.getPrice() + 20.00;
    }
}
