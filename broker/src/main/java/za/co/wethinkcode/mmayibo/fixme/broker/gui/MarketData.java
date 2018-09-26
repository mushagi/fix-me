package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter @Setter
public class MarketData {
    private ArrayList<Instrument> instruments;
    private String name;
    private String id;

    public MarketData(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public MarketData() {
    }
}
