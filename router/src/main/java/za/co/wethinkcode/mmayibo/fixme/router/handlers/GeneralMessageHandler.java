package za.co.wethinkcode.mmayibo.fixme.router.handlers;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageTools;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixTags;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

import java.util.logging.Logger;

public class GeneralMessageHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final ChannelGroupHashed responseChannels;
    private final String rawFixMessage;
    private final String targetCompId;
    private final Channel channel;
    private final Server server;

    GeneralMessageHandler(Server server, String rawFixMessage, String targetCompId, Channel channel) {
        this.responseChannels = server.responseChannels;
        this.channel = channel;
        this.rawFixMessage = rawFixMessage;
        this.targetCompId = targetCompId;
        this.server = server;
    }

    @Override
    public void processMessage() {
        logger.info("Raw Fix Message read : " + rawFixMessage + "" +
                "\nTarget computer {" + targetCompId +"}");

        if (targetCompId != null){
            if (targetCompId.equals("all"))
                server.sendMessage(channel, responseChannels, rawFixMessage);
            else{
                Channel respondChannel = responseChannels.findById(Integer.valueOf(targetCompId));
                server.sendMessage(channel, respondChannel, rawFixMessage);
            }
        }
    }
}