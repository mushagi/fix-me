package za.co.wethinkcode.mmayibo.fixme.market;

import za.co.wethinkcode.mmayibo.fixme.core.client.AbstractClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Market extends AbstractClient {
    public Market(String hostName, int portNumber) throws InterruptedException, ExecutionException, IOException {
        super(hostName, portNumber);
    }
}
