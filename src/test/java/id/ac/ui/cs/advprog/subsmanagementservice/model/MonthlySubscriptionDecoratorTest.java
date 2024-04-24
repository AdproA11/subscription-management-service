package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthlySubscriptionDecoratorTest {

    @Test
    void getDescription() {
        // Arrange
        Subscription basicSubscription = new BasicSubscription("1", 20.00);
        SubscriptionDecorator monthlySubscriptionDecorator = new MonthlySubscriptionDecorator(basicSubscription);

        // Act
        String description = monthlySubscriptionDecorator.getDescription();

        // Assert
        assertEquals("Monthly Subscription", description);
    }

    @Test
    void getPrice() {
        // Arrange
        Subscription basicSubscription = new BasicSubscription("1", 20.00);
        SubscriptionDecorator monthlySubscriptionDecorator = new MonthlySubscriptionDecorator(basicSubscription);

        // Act
        double price = monthlySubscriptionDecorator.getPrice();

        // Assert
        assertEquals(50.00, price);
    }
}
