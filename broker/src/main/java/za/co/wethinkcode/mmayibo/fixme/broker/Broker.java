package za.co.wethinkcode.mmayibo.fixme.broker;

import za.co.wethinkcode.mmayibo.fixme.core.client.AbstractClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Broker extends AbstractClient {
    public Broker(String hostName, int portNumber) throws InterruptedException, ExecutionException, IOException {
        super(hostName, portNumber);
    }
}
