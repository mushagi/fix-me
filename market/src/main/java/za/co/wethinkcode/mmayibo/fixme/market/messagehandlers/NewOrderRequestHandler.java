package za.co.wethinkcode.mmayibo.fixme.market.messagehandlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.model.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.market.Global;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

public class NewOrderRequestHandler implements FixMessageHandlerResponse {
    Channel channel;
    private IRepository repository = Global.repository;

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, FixMessage fixMessage, MarketClient marketClient) {
        this.channel = ctx.channel();
        processBuyRequest(fixMessage);
    }

    private void processBuyRequest(FixMessage fixMessage){
        boolean isPurchased = purchase(fixMessage);
        if (isPurchased)
            sendBuySuccessResponse(fixMessage);
        else
            sendRejectResponse(fixMessage);
    }

    private void sendRejectResponse(FixMessage fixMessage) {

    }

    private void sendBuySuccessResponse(FixMessage fixMessage) {
        FixMessage responseFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("")
                .withSymbol(fixMessage.getSymbol())
                .withTargetCompId(fixMessage.getSenderCompId())
                .withSenderCompId(fixMessage.getSenderCompId())
                .getFixMessage();

        String responseFixString = FixEncode.encode(responseFixMessage);
        channel.writeAndFlush(responseFixString + "\r\n");
    }

    private boolean purchase(FixMessage fixMessage) {
        InstrumentModel instrument = repository.<InstrumentModel>getByID(fixMessage.getSymbol());
        BrokerUser user =  repository.<BrokerUser>getByID(fixMessage.getClientId());

        if (instrument == null || user == null)
            return false;

        if (canInstrumentBeBoughtWithAmount(instrument, fixMessage.getPrice()))
            return false;
        if (!clientHasEnoughMoney(user, fixMessage.getPrice()))
            return false;
        if (!quantityEnoughAndAllowed( instrument, fixMessage.getOrderQuantity()))
            return false;
        user.setAccountBalance(user.getAccountBalance() - fixMessage.getPrice());
        instrument.setQuantity(instrument.getQuantity() - fixMessage.getOrderQuantity());

        if (repository.update(instrument)  == null) {

        }
        if (repository.update(user) == null){

            return false;
        }

        return true;
    }

    private boolean quantityEnoughAndAllowed(InstrumentModel instrument, int orderQuantity) {
        //TODO set allowed quantitity
        return true;
    }

    private boolean clientHasEnoughMoney(BrokerUser user, double price) {
        return user.getAccountBalance() >= price;
    }

    private boolean canInstrumentBeBoughtWithAmount(InstrumentModel instrument, double price) {
        return price >= instrument.getPrice();
    }
}
