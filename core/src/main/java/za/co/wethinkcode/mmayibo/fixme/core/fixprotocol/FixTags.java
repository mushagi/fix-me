package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

public enum FixTags {
    BEGIN_STRING(8),
    BODY_LENGTH(9),
    CHECK_SUM(10),
    CL_ORDER_ID(37),
    MESSAGE_TYPE (35),
    ORDER_QUANTITY(38),
    PRICE(44),
    SENDER_COMP_ID(49),
    SENDING_TIME(52),
    SIDE(54),
    SYMBOL(55),
    TARGET_COMP_ID(56),
    MESSAGE(201),
    MD_REQ_ID(262),
    MD_NAME(500),
    CLIENTID(501),
    WALLET(502),;


    public final int tag;

    FixTags(int tag) {
        this.tag = tag;
    }
}