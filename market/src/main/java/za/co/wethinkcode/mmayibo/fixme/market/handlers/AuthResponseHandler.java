package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

import java.util.logging.Logger;

public class AuthResponseHandler implements FixMessageHandlerResponse{
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, FixMessage fixMessage, MarketClient marketClient) throws InterruptedException {
        logger.info("Auth response status :" + fixMessage.getAuthStatus());
        if (fixMessage.getAuthStatus().equals("success"))
            marketClient.loggedInSuccessfully();
        else
            marketClient.failedToLogin("Error :" + fixMessage.getMessage());
    }
}
