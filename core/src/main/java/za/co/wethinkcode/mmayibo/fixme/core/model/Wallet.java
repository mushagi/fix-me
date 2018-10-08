package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;

import java.util.ArrayList;

@Getter @Setter
public class Wallet  {
    private double availableFunds;
    private ArrayList<OwnedInstrument> ownedInstruments;

    public void updateInstruments(ArrayList<OwnedInstrument> ownedInstruments) {
        for (OwnedInstrument instrument : ownedInstruments) {
            for (OwnedInstrument localOwnedInstrument: this.ownedInstruments) {
                if (localOwnedInstrument.getName().equals(instrument.getName())){
                    localOwnedInstrument.setQuantity(instrument.getQuantity());
                }
                else{
                    this.ownedInstruments.add(instrument);
                }
            }
        }
    }
}
