package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Objects;

@Getter @Setter
public class MarketModel {
    private HashMap<String, InstrumentModel> instruments;
    private String name;
    private String id;

    public MarketModel() {
        instruments = new HashMap<>();
    }

    public MarketModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketModel)) return false;
        MarketModel that = (MarketModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateInstruments(HashMap<String, InstrumentModel> instruments) {
        for (String key : this.instruments.keySet()) {
            InstrumentModel localInstrumentModel = this.instruments.get(key);
            InstrumentModel instrumentModel = instruments.get(key);

            localInstrumentModel.setPrice(instrumentModel.getPrice());
        }
    }
}