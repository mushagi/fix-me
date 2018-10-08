package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OwnedInstrument {
    private String name;
    private int Quantity;

    public OwnedInstrument(String name, int quantity) {
        this.name = name;
        Quantity = quantity;
    }
}