package za.co.wethinkcode.mmayibo.fixme.core.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public abstract class AbstractClient {
    private String hostName;
    private int portNumber;
    AsynchronousSocketChannel client;
    private Logger logger = Logger.getLogger(getClass().getName());


    public AbstractClient(String hostName, int portNumber) throws InterruptedException, ExecutionException, IOException {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public void openConnection() throws IOException, ExecutionException, InterruptedException {
        client = AsynchronousSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress(hostName, portNumber);
        logger.info("Client channel open. HostAddress: " + hostName + " port number: "+ portNumber);
        
        Future future = client.connect(hostAddress);
        future.get();
    }

    public void sendMessage(String message) throws IOException, ExecutionException, InterruptedException {
	    openConnection();
	    ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
	    logger.info("Sending message to broker " + new String(byteBuffer.array()));
	    client.write(byteBuffer);
	    client.close();
	
    }
    
    private void closeConnection() throws IOException {
    }
}
