package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;

@Getter @Setter
public class MarketData {
    private ArrayList<Instrument> instruments;
    private String name;
    private String id;

    MarketData() {
        instruments = new ArrayList<>();
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
}
