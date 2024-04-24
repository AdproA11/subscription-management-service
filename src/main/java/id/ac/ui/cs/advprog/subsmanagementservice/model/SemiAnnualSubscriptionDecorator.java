package id.ac.ui.cs.advprog.subsmanagementservice.model;

public class SemiAnnualSubscriptionDecorator extends SubscriptionDecorator {
    public SemiAnnualSubscriptionDecorator(Subscription subscription) {
        super(subscription);
    }
    @Override
    public String getDescription() {
        return super.getDescription() + "Semi-Annual Subscription";
    }
    @Override
    public double getPrice() {
        return super.getPrice() + 10.00;
    }
}
