package za.co.wethinkcode.mmayibo.fixme.core.router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynchronousRouter implements IRouter{
    private String hostName;
    private int portNumber;

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

    public void runServer() {
        while (clientChannel != null && clientChannel.isOpen()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(32);
            Future<Integer> result = clientChannel.read(byteBuffer);

            while (!result.isDone()) {

            }

            String message = new String(byteBuffer.array());
            if (message.contains("am"))
                System.out.println("Server says : " + message.trim());

            byteBuffer.clear();
        }
    }

    public void initialise() throws IOException, ExecutionException, InterruptedException {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(hostName, portNumber));

        acceptResult = serverSocketChannel.accept();
        clientChannel = acceptResult.get();
    }

    public void handleBrokerMessage() {

    }

    public void handleMarketMessage() {

    }
}
