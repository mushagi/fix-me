package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
class OwnedInstrumentModel {
    private String name;
    private int Quantity;

    public OwnedInstrumentModel(String name, int quantity) {
        this.name = name;
        Quantity = quantity;
    }
}