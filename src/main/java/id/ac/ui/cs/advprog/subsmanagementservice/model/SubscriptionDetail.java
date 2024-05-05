package id.ac.ui.cs.advprog.subsmanagementservice.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubscriptionDetail {
    private Long id;
    private String subscriptionCode;
    private String ownerUsername;
    private Long boxId;
    private String boxName;
    private String type;
    private String status;
    private Double total;
}
