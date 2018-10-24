package za.co.wethinkcode.mmayibo.fixme.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
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

    protected Server(String host, int port, ChannelGroupHashed channels, ChannelGroupHashed responseChannels) {
        HOST = System.getProperty("host", host);
        PORT = Integer.parseInt(System.getProperty("port", String.valueOf(port)));
        this.channels = channels;
        this.responseChannels = responseChannels;
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
                    .childHandler(new ServerInitializer(channels, this))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.bind(HOST, PORT).sync().channel();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

   }
    public void sendIdToClient(Channel channel, int id) {
        FixMessage responseIdBackToChannelFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("1")
                .withText(String.valueOf(id))
                .getFixMessage();

        String fixStringResponseBackToChannel = FixEncode.encode(responseIdBackToChannelFixMessage);
        channel.writeAndFlush(fixStringResponseBackToChannel + "\r\n");
    }
    public abstract int generateId();

   public abstract void messageRead(String rawFixMessage, ChannelHandlerContext ctx);
    protected abstract void channelActive(ChannelHandlerContext ctx, int id);
}