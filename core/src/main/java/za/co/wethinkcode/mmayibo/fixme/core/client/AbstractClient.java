package za.co.wethinkcode.mmayibo.fixme.core.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import java.util.concurrent.Future;

public abstract class AbstractClient {
    private String hostName;
    private int portNumber;
    AsynchronousSocketChannel client;


    public AbstractClient(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public void openConnection() throws IOException {
        client = AsynchronousSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress(hostName, portNumber);
        Future future = client.connect(hostAddress);
    }

    public void sendMessage(String message) throws IOException {
        openConnection();
        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Future result = client.write(byteBuffer);
        closeConnection();
    }

    private void closeConnection() throws IOException {
        client.close();
    }
}
