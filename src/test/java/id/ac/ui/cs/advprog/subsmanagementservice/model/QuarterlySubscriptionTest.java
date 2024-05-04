package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuarterlySubscriptionTest {

    @Test
    void generateSubscriptionCode() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType quarterlySubscription = new QuarterlySubscription(basicSubscription);

        // Act
        String code = quarterlySubscription.generateSubscriptionCode();

        // Assert
        assertEquals("QTR-ABC123", code);
    }

    @Test
    void getType() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType quarterlySubscription = new QuarterlySubscription(basicSubscription);

        // Act
        String type = quarterlySubscription.getType();

        // Assert
        assertEquals("Quarterly", type);
    }

    @Test
    void calculateTotal() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType quarterlySubscription = new QuarterlySubscription(basicSubscription);

        // Act
        double total = quarterlySubscription.calculateTotal(100.0);

        // Assert
        assertEquals(85.0, total);
    }
}
