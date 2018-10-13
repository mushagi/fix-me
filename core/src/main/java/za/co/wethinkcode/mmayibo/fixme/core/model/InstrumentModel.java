package za.co.wethinkcode.mmayibo.fixme.core.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@Entity

@XmlRootElement(name = "Instrument")
@XmlAccessorType(XmlAccessType.FIELD)

@Getter @Setter

public class InstrumentModel {

    @Id
    @XmlElement(name =  "Id")
    private String id;

    @XmlElement(name =  "Name")
    public  String name;

    @XmlElement(name =  "Price")
    public  double price;

    @XmlElement(name =  "Quantity")
    public int quantity;



    public InstrumentModel(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public InstrumentModel() {
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstrumentModel)) return false;
        InstrumentModel that = (InstrumentModel) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String toSymbol() {
        return name;
    }
}
