package za.co.wethinkcode.mmayibo.fixme.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter @Getter
public class BrokerUser {
    @Id
    String userName;

    String name;
    double accountBalance;

    public BrokerUser(String userName, String name) {
        this.userName = userName;
        this.name = name;
    }

    public BrokerUser() {

    }
}