package id.ac.ui.cs.advprog.subsmanagementservice.model;

public class MonthlySubscriptionDecorator extends SubscriptionDecorator {
    public MonthlySubscriptionDecorator(Subscription subscription) {
        super(subscription);
    }
    @Override
    public String getDescription() {
        return super.getDescription() + "Monthly Subscription";
    }
    @Override
    public double getPrice() {
        return super.getPrice() + 30.00;
    }
}
