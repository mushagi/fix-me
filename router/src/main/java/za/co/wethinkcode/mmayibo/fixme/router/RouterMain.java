package za.co.wethinkcode.mmayibo.fixme.router;

import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

public class RouterMain {
    public static void main(String args[]){
        MarketRouter marketRouter = new MarketRouter("localhost", 5000, State.channels);
        BrokerRouter brokerRouter = new BrokerRouter("localhost", 5001, State.channels);

        Thread marketThread = new Thread(marketRouter);
        Thread brokerThread = new Thread(brokerRouter);

        marketThread.start();
        brokerThread.start();
        try {
            marketThread.join();
            brokerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}