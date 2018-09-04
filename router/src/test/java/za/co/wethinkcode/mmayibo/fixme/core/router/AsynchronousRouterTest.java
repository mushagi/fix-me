package za.co.wethinkcode.mmayibo.fixme.core.router;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.mmayibo.fixme.core.client.AbstractClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

class AsynchronousRouterTest {

    @Test
    void run() throws InterruptedException, ExecutionException, IOException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                IRouter router = new AsynchronousRouter("localhost", 5002);
                try {
                    router.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        AbstractClient client = new AbstractClient("localhost", 5002) {};
        client.sendMessage("bhello");
    }
}