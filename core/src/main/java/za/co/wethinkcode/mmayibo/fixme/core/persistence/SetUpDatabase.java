package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import za.co.wethinkcode.mmayibo.fixme.core.model.InitData;

import java.io.*;

public class SetUpDatabase implements Runnable {
    IRepository repository = new RepositoryImp();
    InitData initData = loadInitDataFromXML();
    private InputStream is;

    @Override
    public void run() {
        this.initData = loadInitDataFromXML();
        setUpDatabase();
    }

    private InitData loadInitDataFromXML() {
        try {
            File file = new File(getClass().getResource("/InitData.xml").getFile());
            XmlMapper xmlMapper = new XmlMapper();
            String xml = inputStreamToString(new FileInputStream(file));
            InitData initData = xmlMapper.readValue(xml, InitData.class);
            return initData;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setUpDatabase() {
  //      if (repository.<InstrumentModel>getAll() == null)
            repository.createAll(initData.instruments);

   //     if (repository.<MarketModel>getAll() == null)
            repository.createAll(initData.markets);
    }

    public  String inputStreamToString(InputStream is) throws IOException {
        this.is = is;
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }


}
