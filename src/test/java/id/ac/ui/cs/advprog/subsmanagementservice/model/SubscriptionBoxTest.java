package id.ac.ui.cs.advprog.subsmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionBoxTest {

    @Test
    void testConstructorAndGetters() {
        String name = "Basic Box";
        double price = 25.0;

        SubscriptionBox subscriptionBox = new SubscriptionBox(name, price);

        assertEquals(name, subscriptionBox.getName());
        assertEquals(price, subscriptionBox.getPrice());
    }

    @Test
    void testSetters() {
        SubscriptionBox subscriptionBox = new SubscriptionBox();

        String name = "Premium Box";
        double price = 50.0;

        subscriptionBox.setName(name);
        subscriptionBox.setPrice(price);

        assertEquals(name, subscriptionBox.getName());
        assertEquals(price, subscriptionBox.getPrice());
    }
}