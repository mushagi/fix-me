package za.co.wethinkcode.mmayibo.fixme.core.router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class AsynchronousRouter implements IRouter{
    private String hostName;
    private int portNumber;
    private Logger logger = Logger.getLogger(getClass().getName());

    private AsynchronousServerSocketChannel  serverSocketChannel;
    private Future<AsynchronousSocketChannel> acceptResult;
    private AsynchronousSocketChannel clientChannel;

    AsynchronousRouter(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public void run() throws InterruptedException, ExecutionException, IOException {
        initialise();
        runServer();
    }

    public void runServer() throws ExecutionException, InterruptedException, IOException {
        logger.info("Starting server loop ");

        do {
            acceptResult = serverSocketChannel.accept();
            clientChannel = acceptResult.get();

            ByteBuffer byteBuffer = ByteBuffer.allocate(32);
            Future<Integer> result = clientChannel.read(byteBuffer);
            result.get();

            byteBuffer.flip();

            String message = new String(byteBuffer.array()).trim();

            routeMessage(message);
            byteBuffer.clear();
        } while (true);
    }
    
    private void routeMessage(String message) throws IOException {
        if (message.startsWith("stop"))
        {
            clientChannel.close();
            System.exit(0);
        }

        if (message.contains("b"))
            handleBrokerMessage(message.trim());
    }
    
    public void initialise() throws IOException, ExecutionException, InterruptedException {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(hostName, portNumber));

        logger.info("Server listening on: " + hostName + " port number: "+ portNumber);
    }

    public void handleBrokerMessage(String message) {
    	System.out.println("handling broker message : " + message);
    }

    public void handleMarketMessage(String message) {

    }
}
