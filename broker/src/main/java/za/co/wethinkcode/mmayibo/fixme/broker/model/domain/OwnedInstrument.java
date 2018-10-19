package za.co.wethinkcode.mmayibo.fixme.broker.model.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OwnedInstrument {

    private  int quantity;
    private  String id;

    public OwnedInstrument(String id) {
        this.id = id;
    }
}
