package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class FixMessage {
    HashMap<Integer, Object> tagsValuesMap = new HashMap<>();
    char messageType;
    String beginString;
    String bodyLength;
    String senderCompId;
    String targetCompId;
    String sendingTime;
    int orderQuantity;
    String orderType;
    String clOrderId;
    String side;
    String checkSum;
    double price;
    String requestOrResponse;
    String message;
    String mDReqID;
    String symbol;
    String mdName;
    String walletResponse;
    String clientId;
}