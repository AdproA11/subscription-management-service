package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthlySubscriptionTest {

    @Test
    void generateSubscriptionCode() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType monthlySubscription = new MonthlySubscription(basicSubscription);

        // Act
        String code = monthlySubscription.generateSubscriptionCode();

        // Assert
        assertEquals("MTH-ABC123", code);
    }

    @Test
    void getType() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType monthlySubscription = new MonthlySubscription(basicSubscription);

        // Act
        String type = monthlySubscription.getType();

        // Assert
        assertEquals("Monthly", type);
    }

    @Test
    void calculateTotal() {
        // Arrange
        SubscriptionType basicSubscription = new BasicSubscription("ABC123");
        SubscriptionType monthlySubscription = new MonthlySubscription(basicSubscription);

        // Act
        double total = monthlySubscription.calculateTotal(100.0);

        // Assert
        assertEquals(90.0, total);
    }
}
