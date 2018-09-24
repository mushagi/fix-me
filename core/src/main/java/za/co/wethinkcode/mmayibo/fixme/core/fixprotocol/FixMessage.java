package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class FixMessage {
    HashMap<Integer, String> tagsValuesMap = new HashMap<>();
    String messageType;
    String beginString;
    String bodyLength;
    String senderCompId;
    String targetCompId;
    String sendingTime;
    String orderQuantity;
    String orderType;
    String clOrderId;
    String side;
    String checkSum;
    String price;
}