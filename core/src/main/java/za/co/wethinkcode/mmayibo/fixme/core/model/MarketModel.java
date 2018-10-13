package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@Entity

@XmlRootElement(name = "Instrument")
@XmlAccessorType(XmlAccessType.FIELD)

@Getter @Setter
public class MarketModel {

    @Id
    @XmlElement(name =  "Username")
    private String userName;

    @Transient
    @XmlElementWrapper(name="Instruments")
    @XmlElement(name="InstrumentId")
    private ArrayList<String> instrumentsIds;

    @XmlElement(name =  "Name")
    private String name;


    public MarketModel() {
        instrumentsIds = new ArrayList<>();
    }

    public MarketModel(String name, String userName) {
        this.name = name;
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketModel)) return false;
        MarketModel that = (MarketModel) o;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    public void updateInstruments(ArrayList<String> instrumentsIds) {
        this.instrumentsIds.clear();
        this.instrumentsIds.addAll(instrumentsIds);
    }

}