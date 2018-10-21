package za.co.wethinkcode.mmayibo.fixme.router.handlers;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

import java.util.logging.Logger;

public class GeneralMessageHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final ChannelGroupHashed responseChannels;
    private final String rawFixMessage;
    private final String targetCompId;
    private final Channel channel;

    GeneralMessageHandler(Server server, String rawFixMessage, String targetCompId, Channel channel) {
        this.responseChannels = server.responseChannels;
        this.channel = channel;
        this.rawFixMessage = rawFixMessage;
        this.targetCompId = targetCompId;
    }

    @Override
    public void processMessage() {
        if (targetCompId != null){
            logger.info("Raw Fix Message read : " + rawFixMessage + "" +
                    "\nTarget computer {" + targetCompId +"}");

            if (targetCompId.equals("all"))
                responseChannels.writeAndFlush(  rawFixMessage + "\r\n");
            else {
                Channel respondChannel = responseChannels.find(targetCompId);

                if (respondChannel != null)
                    respondChannel.writeAndFlush(  rawFixMessage + "\r\n");
                else {
                }

            }
        }
    }
}