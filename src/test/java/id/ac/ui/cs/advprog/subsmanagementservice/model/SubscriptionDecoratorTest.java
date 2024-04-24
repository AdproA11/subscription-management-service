package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscriptionDecoratorTest {

    @Test
    void testGetId() {
        // Arrange
        Subscription mockSubscription = mock(Subscription.class);
        when(mockSubscription.getId()).thenReturn("123");

        SubscriptionDecorator subscriptionDecorator = new ConcreteSubscriptionDecorator(mockSubscription);

        // Act
        String id = subscriptionDecorator.getId();

        // Assert
        assertEquals("123", id);
    }

    @Test
    void testGetDescription() {
        // Arrange
        Subscription mockSubscription = mock(Subscription.class);
        when(mockSubscription.getDescription()).thenReturn("Mock Description");

        SubscriptionDecorator subscriptionDecorator = new ConcreteSubscriptionDecorator(mockSubscription);

        // Act
        String description = subscriptionDecorator.getDescription();

        // Assert
        assertEquals("Mock Description", description);
    }

    @Test
    void testGetPrice() {
        // Arrange
        Subscription mockSubscription = mock(Subscription.class);
        when(mockSubscription.getPrice()).thenReturn(100.0);

        SubscriptionDecorator subscriptionDecorator = new ConcreteSubscriptionDecorator(mockSubscription);

        // Act
        double price = subscriptionDecorator.getPrice();

        // Assert
        assertEquals(100.0, price);
    }

    // ConcreteSubscriptionDecorator class untuk testing
    private static class ConcreteSubscriptionDecorator extends SubscriptionDecorator {
        public ConcreteSubscriptionDecorator(Subscription subscription) {
            super(subscription);
        }
    }
}
