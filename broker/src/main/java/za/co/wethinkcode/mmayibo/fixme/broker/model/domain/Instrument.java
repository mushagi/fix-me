package za.co.wethinkcode.mmayibo.fixme.broker.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;

@Getter @Setter

public class Instrument {
    private ArrayList<Double> pricesHistory = new ArrayList<>();
    private String id;
    public  String name;
    public  double costPrice;


    @Override
    public String toString() {
        return name;
    }

    private void addToHistory(double price){
        if (pricesHistory.size() == 20)
            pricesHistory.remove(0);
        pricesHistory.add(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instrument)) return false;
        Instrument that = (Instrument) o;
        return Objects.equals(id, that.id);
    }

    public Instrument(String id, String name, double costPrice) {
        this.id = id;
        this.name = name;
        this.costPrice = costPrice;
        addToHistory(costPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
        addToHistory(costPrice);
    }
}