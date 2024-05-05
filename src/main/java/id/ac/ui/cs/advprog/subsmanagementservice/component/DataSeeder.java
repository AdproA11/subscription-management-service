package id.ac.ui.cs.advprog.subsmanagementservice.component;

import id.ac.ui.cs.advprog.subsmanagementservice.model.Item;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private SubscriptionBoxRepository boxRepository;

    @Override
    public void run(String... args) throws Exception {
        // Real Madrid themed box
        SubscriptionBox box1 = new SubscriptionBox("Real Madrid Box", "Random Real Madrid Merchandise", 250.000);
        Item item1 = new Item("Real Madrid Home 2023/2024");
        Item item2 = new Item("Real Madrid Away 2023/2024");
        box1.setItems(new ArrayList<>(Arrays.asList(item1, item2)));
        boxRepository.save(box1);

        // Manchester United themed box
        SubscriptionBox box2 = new SubscriptionBox("Manchester United Box", "Random Manchester United Merchandise", 500.000);
        Item item3 = new Item("Manchester United Home 2023/2024");
        Item item4 = new Item("Manchester United Away 2023/2024");
        box2.setItems(new ArrayList<>(Arrays.asList(item3, item4)));
        boxRepository.save(box2);

        // Barcelona themed box
        SubscriptionBox box3 = new SubscriptionBox("Barcelona Box", "Random Barcelona Merchandise", 750.000);
        Item item5 = new Item("Barcelona Home 2023/2024");
        Item item6 = new Item("Barcelona Away 2023/2024");
        box3.setItems(new ArrayList<>(Arrays.asList(item5, item6)));
        boxRepository.save(box3);
    }
}
