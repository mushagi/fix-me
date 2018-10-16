package za.co.wethinkcode.mmayibo.fixme.data.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OwnedInstrumentModel {
    private String name;
    private int Quantity;

    public OwnedInstrumentModel(String name, int quantity) {
        this.name = name;
        Quantity = quantity;
    }
}