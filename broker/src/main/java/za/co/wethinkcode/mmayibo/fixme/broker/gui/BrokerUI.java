package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.stage.Stage;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BrokerUI {
    Broker broker;
    ConcurrentHashMap<String, Market> markets;
    ConcurrentHashMap<UUID, TradeTransaction> transactions;
    Stage stage;

    public abstract void update();

    void setUpUi(Broker broker, Stage stage) {
        this.broker = broker;
        this.markets = broker.markets;
        this.transactions = broker.transactions;
        this.stage = stage;
        broker.register(this);
    }

    void unregisterFromBroker() {
        broker.unregisterUi(this);
    }

    public abstract void updateTransactions(TradeTransaction tradeTransaction);
}