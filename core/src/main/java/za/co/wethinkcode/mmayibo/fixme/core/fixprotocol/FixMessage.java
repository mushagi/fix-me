package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import lombok.Getter;

public class FixMessage {
    @Getter
    String message;

    @Getter
    String receiverChannelID;

    @Getter
    String senderChannelID;
}