package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Instrument {
    private  String name;
    private  double price;
    private double maxPrice = 100;
    private double minPrice = 0;

    public Instrument(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }
}
