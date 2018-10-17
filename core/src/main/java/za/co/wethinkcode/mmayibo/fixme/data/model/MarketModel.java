package za.co.wethinkcode.mmayibo.fixme.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Entity

@XmlRootElement(name = "Instrument")
@XmlAccessorType(XmlAccessType.FIELD)

@Getter @Setter
public class MarketModel {

    @Id
    @XmlElement(name =  "Username")
    private String userName;

    @XmlElementWrapper(name="Instruments")
    @XmlElement(name="Instrument")
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<InstrumentModel> instruments;

    public ConcurrentHashMap<String, InstrumentModel> instrumentsHashMap = new ConcurrentHashMap<>();

    @XmlElement(name =  "Name")
    private String name;

    public MarketModel() {
    }

    public MarketModel(String name, String userName, Collection<InstrumentModel> instruments) {
        this.name = name;
        this.userName = userName;
        this.instruments = instruments;
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

    public void updateHashMap() {
        for (InstrumentModel model : instruments)
            instrumentsHashMap.put(model.getId(), model);
    }
}