package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;

public class Instrument {
    @Getter
    private  String name;
    @Getter
    private  double price;
    public Instrument(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
