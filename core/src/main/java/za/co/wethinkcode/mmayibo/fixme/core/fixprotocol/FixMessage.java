package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import io.netty.channel.ChannelId;
import lombok.Getter;

public class FixMessage {

    @Getter
    String message;

    @Getter
    int receiverChannelID;

    @Getter
    int senderChannelID;
}
