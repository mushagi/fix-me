package za.co.wethinkcode.mmayibo.fixme.market;

import io.netty.util.concurrent.Future;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.model.MarketModel;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

import java.util.concurrent.ExecutionException;

class Main {

    public static void main(String args[]) throws ExecutionException, InterruptedException {
        String marketUserName = getMarketIdFromArguments(args);

        MarketClient marketClient = new MarketClient("localhost", 5000, marketUserName);

        new Thread(marketClient).start();
    }

    private static String getMarketIdFromArguments(String[] args) {
        if  (args.length != 1){
            System.out.println("Usage:\n\t\t\t./market [Username]");
            System.exit(0);
        }
        return args[0];
    }
}