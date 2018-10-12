package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Getter @Setter
public class MarketModel {
    @Id
    @Column(updatable = false, nullable = false, length = 100)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private HashMap<String, InstrumentModel> instruments;
    private String name;
    private String userName;

    public MarketModel() {
        instruments = new HashMap<>();
    }

    public MarketModel(String name, String userName) {
        this.name = name;
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketModel)) return false;
        MarketModel that = (MarketModel) o;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    public void updateInstruments(HashMap<String, InstrumentModel> instruments) {
        for (String key : this.instruments.keySet()) {
            InstrumentModel localInstrumentModel = this.instruments.get(key);
            InstrumentModel instrumentModel = instruments.get(key);

            localInstrumentModel.setPrice(instrumentModel.getPrice());
        }
    }

}