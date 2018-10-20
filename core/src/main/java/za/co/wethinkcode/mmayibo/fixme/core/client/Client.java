package za.co.wethinkcode.mmayibo.fixme.core.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import za.co.wethinkcode.mmayibo.fixme.core.ResponseFuture;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.HibernateRepository;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;

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
        this.repository = new HibernateRepository();

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

    protected void sendRequest(FixMessage message){
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

    private String generateMessageId() {
        return UUID.randomUUID().toString();
    }

    public abstract void messageRead(ChannelHandlerContext ctx, FixMessage message, String rawFixMessage) throws InterruptedException;
    public abstract void channelActive(ChannelHandlerContext ctx) throws ExecutionException, InterruptedException;
}
