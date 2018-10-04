package za.co.wethinkcode.mmayibo.fixme.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Stack;

@Getter @Setter
public class Instrument {
    protected  String name;
    protected   double price;


    public Instrument(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instrument)) return false;
        Instrument that = (Instrument) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
