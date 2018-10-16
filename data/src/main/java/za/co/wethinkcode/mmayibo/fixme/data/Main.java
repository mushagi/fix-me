package za.co.wethinkcode.mmayibo.fixme.data;

import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.HibernateRepository;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.SetUpDatabase;


public class Main {
    public static void main(String args[]) {
        IRepository repository = new HibernateRepository();

        SetUpDatabase setUpDatabase = new SetUpDatabase(repository);
        DataClient dataClient = new DataClient("localhost", 5002, repository);

        setUpDatabase.run();
        dataClient.run();
    }
}