package za.co.wethinkcode.mmayibo.fixme.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import za.co.wethinkcode.mmayibo.fixme.core.FixMeChannelInitializer;
import za.co.wethinkcode.mmayibo.fixme.core.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.*;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.HibernateRepository;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class Client implements Runnable {
    final ResponseFuture responseFuture = new ResponseFuture();
    public final IRepository repository;
    private ClientHandler clientHandler = new ClientHandler(this);

    private final String HOST;
    private final int PORT;
    boolean isActive;

    public String networkId = "-1";
    private  Bootstrap bootstrap;

    private EventLoopGroup group = new NioEventLoopGroup();
    boolean isBeingShutdown = false;

    private final Logger logger = Logger.getLogger(getClass().getName());



    protected Client(String host, int port, String username) {
        this.repository = new HibernateRepository(username);

        HOST = System.getProperty("host", host);
        PORT = Integer.parseInt(System.getProperty("port", String.valueOf(port)));
    }

    @Override
    public void run() {
        initBootstrap();
        doConnect();
    }

    private void initBootstrap() {
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new FixMeChannelInitializer(clientHandler))
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
        sendRequest(registerIdMessage, true, false);
    }

    public void sendRequest(FixMessage message, boolean withLogging, boolean withTracking) {
        clientHandler.sendRequest(message, withLogging, withTracking);
    }

    public void sendResponse(FixMessage message, boolean withLogging, boolean withTracking) {
        clientHandler.sendResponse(message, withLogging, withTracking);
    }

    private void connectionEstablished() {
        logger.info("Connection established");
    }

    public void stop() {
        new Thread(() -> {
            logger.info("Shutting down client connection...");
            isBeingShutdown = true;
            group.shutdownGracefully().syncUninterruptibly();
            logger.info("Shutting down successfully");
            System.exit(0);
        }).start();
    }

    public void sendUnsentMessages(String senderCompId){
        clientHandler.sendUnsentMessages(senderCompId);
    }

    public FixMessage  sendMessageWaitForResponse(FixMessage message, boolean logging) throws InterruptedException {
        sendRequest(message, logging, false);
        return responseFuture.get(message.getMessageId());
    }

    public abstract void messageRead(FixMessage message, String rawFixMessage);
    public abstract void channelActive();
}
