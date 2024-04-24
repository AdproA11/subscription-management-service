package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemiAnnualSubscriptionDecoratorTest {

    @Test
    void getDescription() {
        // Arrange
        Subscription basicSubscription = new BasicSubscription("1", 20.00);
        SubscriptionDecorator semiAnnualSubscriptionDecorator = new SemiAnnualSubscriptionDecorator(basicSubscription);

        // Act
        String description = semiAnnualSubscriptionDecorator.getDescription();

        // Assert
        assertEquals("Semi-Annual Subscription", description);
    }

    @Test
    void getPrice() {
        // Arrange
        Subscription basicSubscription = new BasicSubscription("1", 20.00);
        SubscriptionDecorator semiAnnualSubscriptionDecorator = new SemiAnnualSubscriptionDecorator(basicSubscription);

        // Act
        double price = semiAnnualSubscriptionDecorator.getPrice();

        // Assert
        assertEquals(30.00, price);
    }
}
