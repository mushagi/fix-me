package za.co.wethinkcode.mmayibo.fixme.core.fixprotocol;

interface FixMessageHandler {
    void next(FixMessageHandler next);
    void routeMessage();
}