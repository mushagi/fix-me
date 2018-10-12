package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import org.junit.jupiter.api.Test;

class RepositoryImpTest {

    @Test
    void getAll() {
        SetUpDatabase setUpDatabase = new SetUpDatabase();
        setUpDatabase.run();
    }

    @Test
    void create() {
    }
}