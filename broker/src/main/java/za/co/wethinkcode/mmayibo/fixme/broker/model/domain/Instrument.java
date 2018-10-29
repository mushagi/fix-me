package za.co.wethinkcode.mmayibo.fixme.broker.model.domain;

import lombok.Getter;
import lombok.Setter;
import za.co.wethinkcode.mmayibo.fixme.core.ResponseFuture;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

@Getter @Setter

public class Instrument {
    private ArrayList<Double> pricesHistory = new ArrayList<>();
    private ArrayList<Double> predictionHistory = new ArrayList<>();

    private String id;
    public  String name;
    public  double costPrice;
    private Random random = new Random();


    @Override
    public String toString() {
        return name;
    }

    private void addToHistory(double price){
        if (pricesHistory.size() == 20)
            pricesHistory.remove(0);
        pricesHistory.add(price);
    }

    private void addToPredictionHistory(double price){
        if (pricesHistory.size() == 21)
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
        addToPredictionHistory(costPrice);
        addToPredictionHistory(predictHistory());
    }

    private double predictHistory() {
        return 0 + (100- 1) * random.nextDouble();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
        addToHistory(costPrice);
        addToPredictionHistory(predictHistory());
    }

}