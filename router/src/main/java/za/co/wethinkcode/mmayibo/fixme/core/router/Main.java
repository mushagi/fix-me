package za.co.wethinkcode.mmayibo.fixme.core.router;

public class Main {
    public static void main(String args[]){

        MarketRouter marketRouter = new MarketRouter("localhost", 5030);
        BrokerRouter brokerRouter = new BrokerRouter("localhost", 5031);

        new Thread(marketRouter).start();
        new Thread(brokerRouter).start();
    }
}
