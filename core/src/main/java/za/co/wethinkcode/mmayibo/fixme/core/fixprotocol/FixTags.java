package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

public enum FixTags {
    BEGIN_STRING(8),
    BODY_LENGTH(9),
    CHECK_SUM(10),
    CL_ORDER_ID(0),
    MESSAGE_TYPE (35),
    ORDER_QUANTITY(40),
    ORDER_TYPE(44),
    PRICE(49),
    SENDER_COMP_ID(52),
    SENDING_TIME(54),
    SIDE(56),
    TARGET_COMP_ID(49),
    RESPONSE_TYPE(200),
    MESSAGE(201),
    MD_REQ_ID(262);

    public final int tag;

    FixTags(int tag) {
        this.tag = tag;
    }
}