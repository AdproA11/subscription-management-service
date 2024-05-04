package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SubscriptionBoxTest {

    @Test
    void testConstructorAndGetters() {
        String name = "Manchester City Box";
        String description = "Manchhester City subscription box";
        double price = 25.0;

        SubscriptionBox subscriptionBox = new SubscriptionBox(name, description, price);

        assertNotEquals(name, subscriptionBox.getName());
        assertNotEquals(description, subscriptionBox.getDescription());
        assertNotEquals(price, subscriptionBox.getPrice());
    }

    @Test
    void testSetters() {
        SubscriptionBox subscriptionBox = new SubscriptionBox();

        String name = "Manchester United Box";
        String description = "Manchhester United subscription box";
        double price = 50.0;

        subscriptionBox.setName(name);
        subscriptionBox.setDescription(description);
        subscriptionBox.setPrice(price);

        assertNotEquals(name, subscriptionBox.getName());
        assertNotEquals(description, subscriptionBox.getDescription());
        assertNotEquals(price, subscriptionBox.getPrice());
    }
}
