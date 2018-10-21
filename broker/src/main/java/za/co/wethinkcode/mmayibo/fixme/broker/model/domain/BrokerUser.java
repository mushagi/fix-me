package za.co.wethinkcode.mmayibo.fixme.broker.model.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Setter @Getter
public class BrokerUser {
    @Id
    String userName;

    @Transient
    ConcurrentHashMap<String, OwnedInstrument> instruments;

    public BrokerUser(String userName) {
        this.userName = userName;
        instruments = new ConcurrentHashMap<>();
    }

    public Collection <OwnedInstrument> getInstrumentsCollection(){
        return instruments.values();
    }

    public BrokerUser() {

    }

}