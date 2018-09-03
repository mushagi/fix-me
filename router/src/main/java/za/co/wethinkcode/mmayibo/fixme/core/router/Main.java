package za.co.wethinkcode.mmayibo.fixme.core.router;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String args[]) throws InterruptedException, ExecutionException, IOException {
        IRouter router = new AsynchronousRouter();
        router.run();
    }
}
