package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class FixMessage {
    public String authRequestType;
    HashMap<Integer, Object> tagsValuesMap = new HashMap<>();
    String messageType;
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
    String text;
    String mDReqID;
    String symbol;
    String mdName;
    String walletResponse;
    String clientId;
    String dbTransactionType;
    String dbData;
    String dbTableName;
    String ordStatus;
    String dbStatus;
    String authStatus;
    String messageId;
    String refMsgType;
    String testReqId;

}