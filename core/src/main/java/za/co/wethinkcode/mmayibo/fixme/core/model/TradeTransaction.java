package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Getter @Setter
@Entity
public class TradeTransaction {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    UUID clientOrderId;
    String client;
    String side;
    String orderStatus;
    String symbol;
    String text;
    double price;
    int quantity;

}
