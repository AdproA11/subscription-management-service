package id.ac.ui.cs.advprog.subsmanagementservice.model;

public class QuarterlySubscription extends SubscriptionDecorator {
    public QuarterlySubscription(SubscriptionType subscription) {
        super(subscription);
    }

    @Override
    public String generateSubscriptionCode() {
        return "QTR-" + super.generateSubscriptionCode();
    }

    @Override
    public String getType() {
        return "Quarterly";
    }

    @Override
    public double calculateTotal(double basePrice) {
        return basePrice * 0.85;  // Diskon 15%
    }
}