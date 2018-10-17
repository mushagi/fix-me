package za.co.wethinkcode.mmayibo.fixme.router;

import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

public class RouterMain {
    public static void main(String args[]){
        MarketRouter marketRouter = new MarketRouter("localhost", 5000, State.channels);
        BrokerRouter brokerRouter = new BrokerRouter("localhost", 5001, State.channels);
        DatabaseRouter databaseRouter = new DatabaseRouter("localhost", 5002, State.channels);

        Thread marketThread = new Thread(marketRouter);
        Thread brokerThread = new Thread(brokerRouter);
        Thread databaseThread = new Thread(databaseRouter);

        marketThread.start();
        brokerThread.start();
        databaseThread.start();
        try {
            marketThread.join();
            brokerThread.join();
            databaseThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}