package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.stage.Stage;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.data.model.BrokerUser;

import java.util.concurrent.ConcurrentHashMap;

public abstract class BrokerUI {

    protected Broker broker;
    protected ConcurrentHashMap<String, Market> markets;
    Stage stage;


    public abstract void update();

    public void updateUser(BrokerUser user){

    }

    void setUpUi(Broker broker, Stage stage) {
        this.broker = broker;
        this.markets = broker.markets;
        this.stage = stage;
        broker.register(this);
    }

    protected void unregisterFromBroker() {
        broker.unregisterUi(this);
    }
}
