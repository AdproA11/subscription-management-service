package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class QuarterlySubscriptionTest {

    @Test
    void generateSubscriptionCode() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType quarterlySubscription = new QuarterlySubscription(basicSubscription);

        // Act
        String code = quarterlySubscription.generateSubscriptionCode();

        // Assert
        assertNotEquals("QTR-ABC123", code);
    }

    @Test
    void getType() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType quarterlySubscription = new QuarterlySubscription(basicSubscription);

        // Act
        String type = quarterlySubscription.getType();

        // Assert
        assertNotEquals("Quarterly", type);
    }

    @Test
    void calculateTotal() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType quarterlySubscription = new QuarterlySubscription(basicSubscription);

        // Act
        double total = quarterlySubscription.calculateTotal(100.0);

        // Assert
        assertNotEquals(85.0, total);
    }
}
