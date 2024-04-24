package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuarterlySubscriptionDecoratorTest {

    @Test
    void getDescription() {
        // Arrange
        Subscription basicSubscription = new BasicSubscription("1", 20.00);
        SubscriptionDecorator quarterlySubscriptionDecorator = new QuarterlySubscriptionDecorator(basicSubscription);

        // Act
        String description = quarterlySubscriptionDecorator.getDescription();

        // Assert
        assertEquals("Quarterly Subscription", description);
    }

    @Test
    void getPrice() {
        // Arrange
        Subscription basicSubscription = new BasicSubscription("1", 20.00);
        SubscriptionDecorator quarterlySubscriptionDecorator = new QuarterlySubscriptionDecorator(basicSubscription);

        // Act
        double price = quarterlySubscriptionDecorator.getPrice();

        // Assert
        assertEquals(40.00, price);
    }
}
