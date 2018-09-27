package za.co.wethinkcode.mmayibo.fixme.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.logging.Logger;

public abstract class Client implements Runnable {
    private final String HOST;
    private final int PORT;

    protected Channel lastChannel;
    private Channel channel;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Client(String host, int port) {
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

            channel = bootstrap.connect(HOST, PORT).sync().channel();
            lastChannel = channel;
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

    public void sendMessage(String message){
        logger.finest("Really not important");
    //  channel.writeAndFlush(message + "\r\n");
    }

    public abstract void messageRead(ChannelHandlerContext ctx, String message);
    public abstract void channelActive(ChannelHandlerContext ctx);

}
