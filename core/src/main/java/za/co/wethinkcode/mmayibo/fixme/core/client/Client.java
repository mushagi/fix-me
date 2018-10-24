package za.co.wethinkcode.mmayibo.fixme.core.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import za.co.wethinkcode.mmayibo.fixme.core.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.HibernateRepository;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;

import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class Client implements Runnable {

    private final String HOST;
    private final int PORT;

    public final IRepository repository;
    boolean isActive;
    public String networkId = "-1";
    final ResponseFuture responseFuture = new ResponseFuture();
    private  Bootstrap bootstrap;

    protected Channel channel;
    private final Logger logger = Logger.getLogger(getClass().getName());
    Timer timer = new Timer();

     FixMessageBuilder existingBuilder = new FixMessageBuilder();

    protected Client(String host, int port) {
        this.repository = new HibernateRepository();

        HOST = System.getProperty("host", host);
        PORT = Integer.parseInt(System.getProperty("port", String.valueOf(port)));
    }

    @Override
    public void run() {
        initBootstrap();
        doConnect();
    }

    private void initBootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer(this))
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    void doConnect() {
        try {
            bootstrap.connect(HOST, PORT).addListener((ChannelFuture f) -> {
                if (!f.isSuccess()) {
                    long nextRetryDelay = 3000;
                    f.channel().eventLoop().schedule(this::doConnect, nextRetryDelay, TimeUnit.MILLISECONDS);
                }
                else{
                    channel = f.channel();
                    connectionEstablished();

                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerNetworkId(String idToBeReplaced) {
        FixMessage registerIdMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("800")
                .withText(idToBeReplaced)
                .getFixMessage();
        sendRequest(registerIdMessage, true);
    }

    private void connectionLost() {
        logger.info("Connection lost");
    }

    private void connectionEstablished() {
        logger.info("Connection established");
    }

    public void stop() {
        try {
            if (channel != null)
                channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void sendRequest(FixMessage message, boolean logging){
        if (channel != null && channel.isActive()){
            existingBuilder
                    .existingMessage(message)
                    .withSenderCompId(networkId)
                    .withMessageId(generateMessageId());

            String encodedFixMessage = FixEncode.encode(message);
            channel.writeAndFlush(encodedFixMessage + "\r\n");
            if (logging)
                logger.info("Fix Message request : " + encodedFixMessage);
        }
        else
            if (logging)
                logger.info("Cannot send request. Server can't be reached");

    }

    public void sendResponse(FixMessage responseMessage) {
        if (channel != null && channel.isActive()) {
            String encodedFixMessage = FixEncode.encode(responseMessage);
            channel.writeAndFlush(encodedFixMessage + "\r\n");
            logger.info("Fix Message response : " + encodedFixMessage);
        }
        else
            logger.info("Cannot send response. Server can't be reached");
    }

    public FixMessage sendMessageWaitForResponse(FixMessage message, boolean logging) throws InterruptedException {
        sendRequest(message, logging);
        return responseFuture.get(message.getMessageId());
    }

    private String generateMessageId() {
        return UUID.randomUUID().toString();
    }

    public abstract void messageRead(FixMessage message, String rawFixMessage);
    public abstract void channelActive();
}
