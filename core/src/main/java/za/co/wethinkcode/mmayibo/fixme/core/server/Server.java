package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.FixMeChannelInitializer;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;

import java.util.logging.Logger;

public abstract class Server implements Runnable{
    protected final Logger logger = Logger.getLogger(getClass().getName());

    private final String HOST;
    private final int PORT;

    public final ChannelGroupHashed channels;
    public final ChannelGroupHashed responseChannels;
    private final ServerHandler serverHandler;

    protected Server(String host, int port, ChannelGroupHashed channels, ChannelGroupHashed responseChannels) {
        HOST = System.getProperty("host", host);
        PORT = Integer.parseInt(System.getProperty("port", String.valueOf(port)));
        this.channels = channels;
        this.responseChannels = responseChannels;
        serverHandler = new ServerHandler(this);
    }

    @Override
    public void run() {
        startBootstrapServer();
    }

    private void startBootstrapServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class)
                    .childHandler(new FixMeChannelInitializer( serverHandler))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.bind(HOST, PORT).sync().channel();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

   }
    void sendIdToClient(Channel channel, int id) {
        FixMessage responseIdBackToChannelFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withSenderCompId("router")
                .withMessageType("1")
                .withText(String.valueOf(id))
                .getFixMessage();

        String fixStringResponseBackToChannel = FixEncode.encode(responseIdBackToChannelFixMessage);
        channel.writeAndFlush(fixStringResponseBackToChannel + "\r\n");
    }

    public void sendMessage(Channel senderChannel, Channel targetChannel, String rawFixMessage) {
        serverHandler.sendMessage(senderChannel, targetChannel, rawFixMessage);
    }

    public void sendMessage(Channel senderChannel, ChannelGroup channels, String rawFixMessage) {
        serverHandler.sendMessage(senderChannel, channels, rawFixMessage);

    }

    public void sendMessageStatus(Channel channel, boolean status, String rawFixMessage) {
        serverHandler.sendMessageStatus(channel, status, rawFixMessage);
    }
    public abstract int generateId();
    public abstract void messageRead(String rawFixMessage, ChannelHandlerContext ctx);
    protected abstract void channelActive(ChannelHandlerContext ctx, int id);
}