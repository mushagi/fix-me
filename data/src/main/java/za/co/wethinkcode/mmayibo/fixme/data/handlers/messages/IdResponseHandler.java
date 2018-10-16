package za.co.wethinkcode.mmayibo.fixme.data.handlers.messages;


import za.co.wethinkcode.mmayibo.fixme.data.DataClient;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;

public class IdResponseHandler implements IFixMessageHandler {

    private final FixMessage response;
    private final DataClient dataClient;

    public IdResponseHandler(FixMessage response, DataClient dataClient) {
        this.response = response;
        this.dataClient = dataClient;
    }

    @Override
    public void process() {
        dataClient.networkId = response.getMessage();
        System.out.println("Data Id " + dataClient.networkId);
    }
}