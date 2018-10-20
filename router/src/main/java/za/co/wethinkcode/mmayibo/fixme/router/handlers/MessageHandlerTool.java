package za.co.wethinkcode.mmayibo.fixme.router.handlers;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixTags;
import za.co.wethinkcode.mmayibo.fixme.core.server.Server;

import static za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageTools.getTagValueByRegex;

public class MessageHandlerTool{
    public static IMessageHandler getMessageHandler(String rawFixmessage, Server server) {
        String targetComputer = getTagValueByRegex(rawFixmessage, FixTags.TARGET_COMP_ID.tag);
        return new GeneralMessageHandler(server, rawFixmessage, targetComputer);
    }
}