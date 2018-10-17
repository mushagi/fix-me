package za.co.wethinkcode.mmayibo.fixme.data.fixprotocol;

import lombok.Getter;

import java.util.HashMap;

import static za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixTags.*;

public class FixMessageBuilder {
    @Getter
    private  FixMessage fixMessage;
    private  HashMap<Integer, Object> tagsValuesMap;

    public FixMessageBuilder existingMessage(FixMessage fixMessage) {
        this.fixMessage = fixMessage;
        tagsValuesMap = fixMessage.getTagsValuesMap();
        return this;
    }

    public FixMessageBuilder newFixMessage() {
         fixMessage= new FixMessage();
         tagsValuesMap = fixMessage.getTagsValuesMap();
         return this;
    }
    public FixMessageBuilder withMessageType(String value) {
        fixMessage.messageType = value;
        tagsValuesMap.put(MESSAGE_TYPE.tag, value);
        return  this;
    }

    public FixMessageBuilder withBeginString(String value) {
        fixMessage.beginString = value;
        tagsValuesMap.put(BEGIN_STRING.tag, value);

        return  this;
    }
    public FixMessageBuilder withBodyLength(String value) {
        fixMessage.bodyLength = value;
        tagsValuesMap.put(BODY_LENGTH.tag, value);
        return  this;
    }
    public FixMessageBuilder withSenderCompId(String value) {
        fixMessage.senderCompId = value;
        tagsValuesMap.put(SENDER_COMP_ID.tag, value);
        return  this;
    }

    public FixMessageBuilder withTargetCompId(String value) {
        fixMessage.targetCompId = value;
        tagsValuesMap.put(TARGET_COMP_ID.tag, value);
        return  this;
    }
    public FixMessageBuilder withSendingTime(String value) {
        fixMessage.sendingTime = value;
        tagsValuesMap.put(SENDING_TIME.tag, value);
        return  this;
    }
    public FixMessageBuilder withOrderQuantity(int value) {
        fixMessage.orderQuantity = value;
        tagsValuesMap.put(ORDER_QUANTITY.tag, value);
        return  this;
    }

    public FixMessageBuilder withClOrderId(String value) {
        fixMessage.clOrderId = value;
        tagsValuesMap.put(CL_ORDER_ID.tag, value);
        return  this;
    }
    public FixMessageBuilder withSide(String value) {
        fixMessage.side = value;
        tagsValuesMap.put(SIDE.tag, value);
        return  this;
    }
    public FixMessageBuilder withCheckSum(String value) {
        fixMessage.checkSum = value;
        tagsValuesMap.put(CHECK_SUM.tag, value);
        return  this;
    }
    public FixMessageBuilder withPrice(double value) {
        fixMessage.price = value;
        tagsValuesMap.put(PRICE.tag, value);
        return  this;
    }

    public FixMessageBuilder withMessage(String value) {
        fixMessage.message = value;
        tagsValuesMap.put(MESSAGE.tag, value);
        return this;
    }

    public FixMessageBuilder withMDReqID(String value) {
        fixMessage.mDReqID = value;
        tagsValuesMap.put(MD_REQ_ID.tag, value);
        return this;
    }


    public FixMessageBuilder withMDName(String value) {
        fixMessage.mdName = value;
        tagsValuesMap.put(MD_NAME.tag, value);
        return this;
    }

    public FixMessageBuilder withSymbol(String value) {
        fixMessage.symbol = value;
        tagsValuesMap.put(SYMBOL.tag, value);
        return this;
    }

    public FixMessageBuilder withClientIId(String value) {
        fixMessage.clientId = value;
        tagsValuesMap.put(CLIENTID.tag, value);
        return this;
    }
    public FixMessageBuilder withWallet(String value) {
        fixMessage.walletResponse = value;
        tagsValuesMap.put(WALLET.tag, value);
        return this;
    }
    public FixMessageBuilder withOrdStatus(String value) {
        fixMessage.ordStatus = value;
        tagsValuesMap.put(ORD_STATUS.tag, value);
        return this;
    }
    public FixMessageBuilder withDbStatus(String value) {
        fixMessage.dbStatus = value;
        tagsValuesMap.put(DBSTATUS.tag, value);
        return this;
    }
    public FixMessageBuilder withDbTable(String value) {
        fixMessage.dbTableName = value;
        tagsValuesMap.put(DBTABLE.tag, value);
        return this;
    }

    public FixMessageBuilder withDbData(String value) {
        fixMessage.dbData = value;
        tagsValuesMap.put(DBDATA.tag, value);
        return this;
    }

    public FixMessageBuilder withDbTransactionType(String value) {
        fixMessage.dbTransactionType = value;
        tagsValuesMap.put(DB_TRANSACTION_TYPE.tag, value);
        return this;
    }

    public FixMessageBuilder withAuthStatus(String value) {
        fixMessage.authStatus = value;
        tagsValuesMap.put(AUTH_STATUS.tag, value);
        return this;
    }

    public FixMessageBuilder withMessageId(String value) {
        fixMessage.messageId = value;
        tagsValuesMap.put(MSG_ID.tag, value);
        return this;
    }

    public FixMessageBuilder withAuthRequestType(String value) {
        fixMessage.authRequestType = value;
        tagsValuesMap.put(AUTH_REQUEST_TYPE.tag, value);
        return this;
    }

    public FixMessageBuilder withRefMsgType	(String value) {
        fixMessage.authRequestType = value;
        tagsValuesMap.put(REF_MSG_TYPE.tag, value);
        return this;
    }
}
