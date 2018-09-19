package za.co.wethinkcode.mmayibo.fixme.core.router;

public class Main {
    public static void main(String args[]){

        Router marketRouter = new Router("localhost", 5030, State.marketChannels, State.brokerChannels );
        Router brokerRouter = new Router("localhost", 5031, State.brokerChannels, State.marketChannels);

        new Thread(marketRouter).start();
        new Thread(brokerRouter).start();
    }
}
