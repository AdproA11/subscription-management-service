package id.ac.ui.cs.advprog.subsmanagementservice;


import id.ac.ui.cs.advprog.subsmanagementservice.model.Item;
import id.ac.ui.cs.advprog.subsmanagementservice.model.SubscriptionBox;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.ItemRepository;
import id.ac.ui.cs.advprog.subsmanagementservice.repository.SubscriptionBoxRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class SubsManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubsManagementServiceApplication.class, args);
    }
    @Bean
    public CommandLineRunner initData(SubscriptionBoxRepository subscriptionBoxRepository, ItemRepository itemRepository) {
        return args -> { //testing with DB locally
            if (subscriptionBoxRepository.count() == 0) {
                SubscriptionBox box1 = new SubscriptionBox("Real Madrid Box", 29.99, "Random Real Madrid Merchandise");
                Item item1 = new Item("Real Madrid Home 2023/2024", "Offical Jersey");
                Item item2 = new Item("Real Madrid Away 2023/2024", "Offical Jersey");
                item1.setSubscriptionBox(box1);
                item2.setSubscriptionBox(box1);
                box1.setItems(new HashSet<>(Arrays.asList(item1, item2)));
                subscriptionBoxRepository.save(box1);

                SubscriptionBox box2 = new SubscriptionBox("Manchester United Box", 49.99, "Random Manchester United Merchandise");
                Item item3 = new Item("Manchester United Home 2023/2024", "Offical Jersey");
                Item item4 = new Item("Manchester United Away 2023/2024", "Offical Jersey");
                item1.setSubscriptionBox(box1);
                item2.setSubscriptionBox(box1);
                box1.setItems(new HashSet<>(Arrays.asList(item3, item4)));
                subscriptionBoxRepository.save(box2);

                SubscriptionBox box3 = new SubscriptionBox("Barcelona Box", 99.99, "Random Barcelona Merchandise");
                Item item5 = new Item("Barcelona Home 2023/2024", "Offical Jersey");
                Item item6 = new Item("Barcelona  Away 2023/2024", "Offical Jersey");
                box3.setItems(new HashSet<>(Arrays.asList(item5, item6)));
                subscriptionBoxRepository.save(box3);


                System.out.println("Loaded dummy subscription boxes with items into database.");
            }
        };
    }

}


