package za.co.wethinkcode.mmayibo.fixme.router.handlers;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.core.ChannelGroupHashed;
import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageTools;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixTags;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

import java.util.logging.Logger;

public class RegisterNetworkId implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final ChannelGroupHashed responseChannels;
    private final String rawFixMessage;
    private final Channel channel;
    private final ChannelGroupHashed channels;
    private final Server server;

    RegisterNetworkId(Server server, String rawFixMessage, Channel channel) {
        this.responseChannels = server.responseChannels;
        this.server = server;
        this.channel = channel;
        this.rawFixMessage = rawFixMessage;
        this.channels = server.channels;
    }

    @Override
    public void processMessage() {
        String senderCompId = FixMessageTools.getTagValueByRegex(rawFixMessage, FixTags.SENDER_COMP_ID.tag);
        if (senderCompId != null) {
            logger.info("Raw Fix Message read : " + rawFixMessage + "" +
                    "\nId to be persisted {" + senderCompId + "}");
            String idToBeRemovedString = FixMessageTools.getTagValueByRegex(rawFixMessage, FixTags.TEXT.tag);

            if (idToBeRemovedString != null) {
                int idToBeRemoved = Integer.parseInt(idToBeRemovedString);
                channels.replaceIds(idToBeRemoved, Integer.valueOf(senderCompId), channel);
            }
        }
    }
}
