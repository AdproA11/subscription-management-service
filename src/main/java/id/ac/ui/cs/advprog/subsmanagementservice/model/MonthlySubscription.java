package id.ac.ui.cs.advprog.subsmanagementservice.model;

public class MonthlySubscription extends SubscriptionDecorator {
    public MonthlySubscription(SubscriptionType subscription) {
        super(subscription);
    }

    @Override
    public String generateSubscriptionCode() {
        return "MTH-" + super.generateSubscriptionCode();
    }

    @Override
    public String getType() {
        return "Monthly";
    }

    @Override
    public double calculateTotal(double basePrice) {
        return basePrice * 0.90;  // Diskon 10%
    }
}




