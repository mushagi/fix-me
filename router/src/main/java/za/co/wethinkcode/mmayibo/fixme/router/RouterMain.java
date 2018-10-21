package za.co.wethinkcode.mmayibo.fixme.router;

class RouterMain {
    public static void main(String args[]){
        MarketRouter marketRouter = new MarketRouter("localhost", 5000, State.marketChannels, State.brokerChannels);
        BrokerRouter brokerRouter = new BrokerRouter("localhost", 5001, State.brokerChannels, State.marketChannels);

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