package za.co.wethinkcode.mmayibo.fixme.core.router;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface IRouter {
    void run() throws InterruptedException, ExecutionException, IOException;
    void initialise() throws IOException, ExecutionException, InterruptedException;
    void handleBrokerMessage();
    void handleMarketMessage();
    void runServer() throws ExecutionException, InterruptedException;
}
