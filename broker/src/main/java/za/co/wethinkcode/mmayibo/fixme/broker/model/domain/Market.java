package za.co.wethinkcode.mmayibo.fixme.broker.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class Market {
    private String name;
    private String mdReqId;
    private String networkId;
    private boolean isOnline;

    private ConcurrentHashMap<String, Instrument> instruments;

    public Market(String name, String mdReqId, ConcurrentHashMap<String, Instrument> instruments, String networkId) {
        this.name = name;
        this.mdReqId = mdReqId;
        this.instruments = instruments;
        this.networkId = networkId;
    }

    public void updateInstruments(ConcurrentHashMap<String, Instrument> updatedInstruments) {
        for (Instrument updatedInstrument: updatedInstruments.values()) {
            Instrument instrument = this.instruments.get(updatedInstrument.getId());
            if (instrument != null)
                instrument.setCostPrice(updatedInstrument.getCostPrice());
            else
                this.instruments.put(updatedInstrument.getId(), updatedInstrument);
        }
    }
}
