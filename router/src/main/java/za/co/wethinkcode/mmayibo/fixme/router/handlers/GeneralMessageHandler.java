package za.co.wethinkcode.mmayibo.fixme.router.handlers;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
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

            if (targetCompId.equals("all")){
                responseChannels.writeAndFlush(  rawFixMessage + "\r\n").addListener(future -> {
                    sendConnectivityStatus(true);
                });

            }
            else {
                Channel respondChannel = responseChannels.findById(Integer.valueOf(targetCompId));

                if (respondChannel != null)
                    respondChannel.writeAndFlush(  rawFixMessage + "\r\n")
                            .addListener(future -> sendConnectivityStatus(future.isSuccess()));
                else
                    logger.info("Target channel doesn't exist ");
            }
        }
    }

    private FixMessageBuilder builder = new FixMessageBuilder()
            .newFixMessage()
            .withMessageType("400");

    private void sendConnectivityStatus(boolean status) {
        if (channel != null){
            String messageId = FixMessageTools.getTagValueByRegex(rawFixMessage, FixTags.MSG_ID.tag);
            String senderCompId = FixMessageTools.getTagValueByRegex(rawFixMessage, FixTags.SENDER_COMP_ID.tag);
            builder.withTestReqId(String.valueOf(status));
            builder.withMessageId(messageId);
            builder.withSenderCompId(senderCompId);
            builder.withTargetCompId(targetCompId);
            String offlineResponse = FixEncode.encode(builder.getFixMessage());
            channel.writeAndFlush(offlineResponse+"\r\n");
        }
    }
}