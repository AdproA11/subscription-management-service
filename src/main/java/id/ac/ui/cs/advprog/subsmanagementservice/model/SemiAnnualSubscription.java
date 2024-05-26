package id.ac.ui.cs.advprog.subsmanagementservice.model;

public class SemiAnnualSubscription extends SubscriptionDecorator {
    public SemiAnnualSubscription(SubscriptionType subscription) {
        super(subscription);
    }
    @Override
    public String generateSubscriptionCode() {
        return "SAA-" + super.generateSubscriptionCode();
    }
    @Override
    public String getType() {
        return "Semi-annual";
    }
    @Override
    public double calculateTotal(double basePrice) {
        return basePrice * 0.80;  // Discount 20%
    }
}
