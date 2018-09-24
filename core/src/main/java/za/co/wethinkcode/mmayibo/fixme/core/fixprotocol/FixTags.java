package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

public enum FixTags {
    BEGIN_STRING(9),
    BODY_LENGTH(10),
    CHECK_SUM(11),
    CL_ORDER_ID(35),
    MESSAGE_TYPE (38),
    ORDER_QUANTITY(40),
    ORDER_TYPE(44),
    PRICE(49),
    SENDER_COMP_ID(52),
    SENDING_TIME(54),
    SIDE(56),
    TARGET_COMP_ID(49);

    public final int tag;

    FixTags(int tag) {
        this.tag = tag;
    }
}