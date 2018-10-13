package za.co.wethinkcode.mmayibo.fixme.market;

import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.RepositoryImp;

class Main {

    public static void main(String args[]) {
        String marketUserName = getMarketIdFromArguments(args);
        IRepository repository = new RepositoryImp(Main.class.getResource("/hibernate.cfg.xml").toString());
        MarketModel marketModel = getMarketFromDatabase(marketUserName, repository);
        MarketClient marketClient = new MarketClient("localhost", 5000, marketModel, repository);
        new Thread(marketClient).start();
    }

    private static MarketModel getMarketFromDatabase(String marketUserName, IRepository repository) {
        MarketModel marketModel = repository.getByID(marketUserName, MarketModel.class);
        if (marketModel == null){
            System.out.println("Market does not exist in the database");
            System.exit(0);
        }
        return marketModel;
    }

    private static String getMarketIdFromArguments(String[] args) {
        if  (args.length != 1){
            System.out.println("Usage:\n\t\t\t./market [Username]");
            System.exit(0);
        }
        return args[0];
    }
}