package za.co.wethinkcode.mmayibo.fixme.market.model;

import lombok.Getter;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;

@Getter
public class MarketInstrument extends Instrument {

    private double maxPrice = 100;
    private double minPrice = 0;

    public MarketInstrument(String name, double price) {
        super(name, price);
    }
}
