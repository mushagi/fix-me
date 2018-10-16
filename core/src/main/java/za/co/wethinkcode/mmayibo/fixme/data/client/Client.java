package za.co.wethinkcode.mmayibo.fixme.data.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import za.co.wethinkcode.mmayibo.fixme.data.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.FixRepositoryImp;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public abstract class Client implements Runnable {

    private final String HOST;
    private final int PORT;
    public final IRepository repository;
    boolean isActive;
    public String networkId;
    ResponseFuture responseFuture = new ResponseFuture();

    protected Channel lastChannel;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Client(String host, int port) {
        this.repository = new FixRepositoryImp(this);

        HOST = System.getProperty("host", host);
        PORT = Integer.parseInt(System.getProperty("port", String.valueOf(port)));
    }

    @Override
    public void run() {
        startClient();
    }

    private void startClient() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitializer(this))
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            lastChannel = bootstrap.connect(HOST, PORT).sync().channel();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (lastChannel != null)
                lastChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public  void sendRequest(FixMessage message){
        new FixMessageBuilder()
                .existingMessage(message)
                .withMessageId(generateMessageId());

        String encodedFixMessage = FixEncode.encode(message);
        lastChannel.writeAndFlush(encodedFixMessage + "\r\n");
        logger.info("Fix Message request : " + encodedFixMessage);
    }

    public void sendResponse(FixMessage responseMessage) {
        String encodedFixMessage = FixEncode.encode(responseMessage);
        lastChannel.writeAndFlush(encodedFixMessage + "\r\n");
        logger.info("Fix Message response : " + encodedFixMessage);

    }

    public FixMessage sendMessageWaitForResponse(FixMessage message) throws InterruptedException {
        sendRequest(message);
        return responseFuture.get(message.getMessageId());
    }

    public FixMessage authRequestMessage(String userName, String clientType, String authRequestType) {
        FixMessage requestFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("authrequest")
                .withAuthRequestType(authRequestType)
                .withDbData(userName)
                .withDbTable(clientType)
                .getFixMessage();
        return requestFixMessage;
    }

    private String generateMessageId() {
        return UUID.randomUUID().toString();
    }


    public abstract void messageRead(ChannelHandlerContext ctx, FixMessage message) throws InterruptedException;
    public abstract void channelActive(ChannelHandlerContext ctx) throws ExecutionException, InterruptedException;
}
