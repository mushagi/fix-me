package za.co.wethinkcode.mmayibo.fixme.data.persistence;

import za.co.wethinkcode.mmayibo.fixme.data.model.InitData;
import za.co.wethinkcode.mmayibo.fixme.data.model.MarketModel;

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
        try {
            setUpDatabase();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadInitDataFromXML() {
        try {
            File file = new File(getClass().getResource("/init.data.xml").getFile());

            JAXBContext jaxbContext = JAXBContext.newInstance(InitData.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            initData = (InitData) unmarshaller.unmarshal(file);
            System.out.println(initData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpDatabase() throws InterruptedException {
        if (repository.getAll(MarketModel.class).isEmpty())
            repository.createAll(initData.markets);
    }



}
