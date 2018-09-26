package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import lombok.Getter;

public class Instrument {
    @Getter
    private  String name;

    public Instrument(String name) {
        this.name = name;
    }
}
