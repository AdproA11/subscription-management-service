package id.ac.ui.cs.advprog.subsmanagementservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subscriptionCode;
    private String ownerUsername;
    private Long boxId;
    private String status;
    private String type;
}
