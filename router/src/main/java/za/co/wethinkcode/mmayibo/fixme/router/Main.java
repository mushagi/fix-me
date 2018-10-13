package za.co.wethinkcode.mmayibo.fixme.router;

import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.RepositoryImp;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.SetUpDatabase;

public class Main {
    public static void main(String args[]){
        IRepository repository = new RepositoryImp(Main.class.getResource("/hibernate.cfg.xml").toString());

        MarketRouter marketRouter = new MarketRouter("localhost", 5000, State.marketChannels, State.brokerChannels );
        BrokerRouter brokerRouter = new BrokerRouter("localhost", 5001, State.brokerChannels, State.marketChannels);
        SetUpDatabase setUpDatabase = new SetUpDatabase(repository);

        Thread setUpDatabaseThread = new Thread(setUpDatabase);
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