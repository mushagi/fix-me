package za.co.wethinkcode.mmayibo.fixme.router;

import za.co.wethinkcode.mmayibo.fixme.core.persistence.SetUpDatabase;

public class Main {
    public static void main(String args[]){

        MarketRouter marketRouter = new MarketRouter("localhost", 5000, State.marketChannels, State.brokerChannels );
        BrokerRouter brokerRouter = new BrokerRouter("localhost", 5001, State.brokerChannels, State.marketChannels);

        Thread setUpDatabaseThread = new Thread(new SetUpDatabase());
        Thread marketThread = new Thread(marketRouter);
        Thread brokerThread = new Thread(brokerRouter);

        setUpDatabaseThread.start();
        marketThread.start();
        brokerThread.start();

        try {
            setUpDatabaseThread.join();
            marketThread.join();
            marketThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}