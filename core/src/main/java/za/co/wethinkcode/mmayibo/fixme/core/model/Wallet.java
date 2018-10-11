package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Wallet  {
    private double availableFunds;
    private ArrayList<OwnedInstrumentModel> ownedInstrumentModels;

    public void updateInstruments(ArrayList<OwnedInstrumentModel> ownedInstrumentModels) {
        for (OwnedInstrumentModel instrument : ownedInstrumentModels) {
            for (OwnedInstrumentModel localOwnedInstrumentModel : this.ownedInstrumentModels) {
                if (localOwnedInstrumentModel.getName().equals(instrument.getName())){
                    localOwnedInstrumentModel.setQuantity(instrument.getQuantity());
                }
                else{
                    this.ownedInstrumentModels.add(instrument);
                }
            }
        }
    }
}
