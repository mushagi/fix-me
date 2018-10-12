package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import za.co.wethinkcode.mmayibo.fixme.core.model.InitData;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;

import java.util.Collection;

public class SetUpDatabase implements Runnable {
    IRepository repository = new RepositoryImp();
    InitData initData;

    @Override
    public void run() {
        this.initData = loadInitDataFromXML();
        setUpDatabase();
    }

    private InitData loadInitDataFromXML() {
        return null;
    }

    private void setUpDatabase() {
        if (repository.<InstrumentModel>getAll() == null)
            repository.createAll(initData.instruments);

        if (repository.<MarketModel>getAll() == null)
            repository.createAll(initData.markets);
    }

}
