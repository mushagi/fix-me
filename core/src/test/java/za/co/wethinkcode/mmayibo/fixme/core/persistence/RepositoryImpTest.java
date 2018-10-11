package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryImpTest {

    @Test
    void getAll() {
        IRepository repository = new RepositoryImp();
        repository.create(new InstrumentModel("dfg", 999));
    }

    @Test
    void create() {
    }
}