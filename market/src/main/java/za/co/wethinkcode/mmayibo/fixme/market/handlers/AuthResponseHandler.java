package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

import java.util.logging.Logger;

public class AuthResponseHandler implements FixMessageHandlerResponse{
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final MarketClient client;

    public AuthResponseHandler(MarketClient client) {
        this.client = client;
    }

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage fixMessage) {
        logger.info("Auth response status :" + fixMessage.getAuthStatus());
        if (fixMessage.getAuthStatus().equals("success")) {
            try {
                client.loggedInSuccessfully();
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.failedToLogin("could not login");
            }
        }
        else
            client.failedToLogin("Error :" + fixMessage.getMessage());
    }
}
