package za.co.wethinkcode.mmayibo.fixme.data.persistence;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;

import java.util.Collection;

class SetUpDatabaseTest {

    @Test
    void run() throws InterruptedException {
        IRepository repository = new HibernateRepository();
        SetUpDatabase setUpDatabase = new SetUpDatabase(repository);

        setUpDatabase.run();

        Collection<InstrumentModel> i = repository.getAll(InstrumentModel.class);

        System.out.println(i);

    }
}