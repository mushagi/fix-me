package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import za.co.wethinkcode.mmayibo.fixme.core.model.InitData;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class SetUpDatabase implements Runnable {
    private IRepository repository;
    private InitData initData;

    public SetUpDatabase(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run() {
        loadInitDataFromXML();
        setUpDatabase();
    }

    private void loadInitDataFromXML() {
        try {
            File file = new File(getClass().getResource("/InitData.xml").getFile());

            JAXBContext jaxbContext = JAXBContext.newInstance(InitData.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            initData = (InitData) unmarshaller.unmarshal(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpDatabase() {
        if (repository.getAll(InstrumentModel.class).isEmpty())
            repository.createAll(initData.instruments);
        if (repository.getAll(MarketModel.class).isEmpty())
           repository.createAll(initData.markets);
    }



}
