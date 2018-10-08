package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.core.model.Wallet;

import java.util.HashMap;

public interface BrokerUI {
    void registerBroker(Broker broker);
    void update();
    void updateWallet(Wallet wallet);
}
