package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemiAnnualSubscriptionTest {

    @Test
    void generateSubscriptionCode() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType semiAnnualSubscription = new SemiAnnualSubscription(basicSubscription);

        // Act
        String code = semiAnnualSubscription.generateSubscriptionCode();

        // Assert
        assertEquals("SAA-ABC123", code);
    }

    @Test
    void getType() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType semiAnnualSubscription = new SemiAnnualSubscription(basicSubscription);

        // Act
        String type = semiAnnualSubscription.getType();

        // Assert
        assertEquals("Semi-annual", type);
    }

    @Test
    void calculateTotal() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType semiAnnualSubscription = new SemiAnnualSubscription(basicSubscription);

        // Act
        double total = semiAnnualSubscription.calculateTotal(100.0);

        // Assert
        assertEquals(80.0, total);
    }
}
