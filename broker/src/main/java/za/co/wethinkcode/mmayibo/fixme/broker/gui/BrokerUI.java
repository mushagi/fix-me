package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.stage.Stage;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;

import java.util.concurrent.ConcurrentHashMap;

public abstract class BrokerUI {

    protected Broker broker;
    ConcurrentHashMap<String, Market> markets;
    Stage stage;


    public abstract void update();


    void setUpUi(Broker broker, Stage stage) {
        this.broker = broker;
        this.markets = broker.markets;
        this.stage = stage;
        broker.register(this);
    }

    void unregisterFromBroker() {
        broker.unregisterUi(this);
    }

}