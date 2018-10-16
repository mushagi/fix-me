package za.co.wethinkcode.mmayibo.fixme.market.model;

import lombok.Getter;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;

@Getter
public class MarketInstrumentModel extends InstrumentModel {

    private double maxPrice = 100;
    private double minPrice = 0;

    public MarketInstrumentModel(String name, double price) {
        super(name, price);
    }
}