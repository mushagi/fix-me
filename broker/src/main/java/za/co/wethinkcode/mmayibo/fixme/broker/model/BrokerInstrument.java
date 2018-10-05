package za.co.wethinkcode.mmayibo.fixme.broker.model;

import lombok.Getter;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;

import java.util.ArrayList;

public class BrokerInstrument extends Instrument {
    @Getter
    private ArrayList<Double> pricesHistory = new ArrayList<>();

    public BrokerInstrument(String name, double price) {
        super(name, price);
        addToHistory(price);
    }

    private void addToHistory(double price){
        if (pricesHistory.size() == 20)
            pricesHistory.remove(0);
        pricesHistory.add(price);
    }

    @Override
    public void setPrice(double price) {
        super.setPrice(price);
        addToHistory(price);
    }
}