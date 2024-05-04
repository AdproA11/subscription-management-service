package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SemiAnnualSubscriptionTest {

    @Test
    void generateSubscriptionCode() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType semiAnnualSubscription = new SemiAnnualSubscription(basicSubscription);

        // Act
        String code = semiAnnualSubscription.generateSubscriptionCode();

        // Assert
        assertNotEquals("SAA-ABC123", code);
    }

    @Test
    void getType() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType semiAnnualSubscription = new SemiAnnualSubscription(basicSubscription);

        // Act
        String type = semiAnnualSubscription.getType();

        // Assert
        assertNotEquals("Semi-annual", type);
    }

    @Test
    void calculateTotal() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType semiAnnualSubscription = new SemiAnnualSubscription(basicSubscription);

        // Act
        double total = semiAnnualSubscription.calculateTotal(100.0);

        // Assert
        assertNotEquals(80.0, total);
    }
}
