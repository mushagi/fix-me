package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Getter @Setter
public class MarketData {
    private HashMap<String, Instrument> instruments;
    private String name;
    private String id;

    public MarketData() {
        instruments = new HashMap<>();
    }

    public MarketData(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketData)) return false;
        MarketData that = (MarketData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateInstruments(HashMap<String, Instrument> instruments) {
        for (String key : this.instruments.keySet()) {
            Instrument localInstrument = this.instruments.get(key);
            Instrument instrument = instruments.get(key);

            localInstrument.setPrice(instrument.getPrice());
        }
    }
}