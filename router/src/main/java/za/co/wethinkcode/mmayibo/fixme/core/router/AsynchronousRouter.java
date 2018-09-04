package za.co.wethinkcode.mmayibo.fixme.core.router;

import sun.rmi.runtime.Log;

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

    public AsynchronousRouter(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public void run() throws InterruptedException, ExecutionException, IOException {
        initialise();
        runServer();
    }

    public void runServer() throws ExecutionException, InterruptedException {
        while (clientChannel != null && clientChannel.isOpen()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(32);
            Future<Integer> result = clientChannel.read(byteBuffer);
	        result.get();
	        
            while (!result.isDone()) {
            }

            String message = new String(byteBuffer.array());
            routeMessage(message);
            
            byteBuffer.clear();
        }
    }
    
    private void routeMessage(String message) {
        if (message.startsWith("b"))
            handleBrokerMessage(message.trim());
    }
    
    public void initialise() throws IOException, ExecutionException, InterruptedException {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(hostName, portNumber));
        
        logger.info("Server channel open. Binded to Hostname: " + hostName + " port number: "+ portNumber);
        
        acceptResult = serverSocketChannel.accept();
        clientChannel = acceptResult.get();
        
    }

    public void handleBrokerMessage(String message) {
    	System.out.println("handling broker message : " + message);
    }

    public void handleMarketMessage(String message) {

    }
}
