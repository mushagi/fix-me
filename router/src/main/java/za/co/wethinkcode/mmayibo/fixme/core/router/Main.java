package za.co.wethinkcode.mmayibo.fixme.core.router;

public class Main {
    public static void main(String args[]){
        MarketRouter marketRouter = new MarketRouter("localhost", 5030, State.marketChannels, State.brokerChannels );
        BrokerRouter brokerRouter = new BrokerRouter("localhost", 5031, State.brokerChannels, State.marketChannels);

        Thread marketThread = new Thread(marketRouter);
        Thread brokerThread = new Thread(brokerRouter);

        marketThread.start();
        brokerThread.start();

        try {
            marketThread.join();
            marketThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}