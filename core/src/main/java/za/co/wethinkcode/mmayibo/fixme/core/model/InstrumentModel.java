package za.co.wethinkcode.mmayibo.fixme.core.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter @Setter
@Entity
public class InstrumentModel {

    @Id
    @Column(updatable = false, nullable = false, length = 100)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public  String name;

    public  double price;
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
