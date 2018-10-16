package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.data.model.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;

public class NewOrderRequestHandler implements FixMessageHandlerResponse {
    private Channel channel;
    private IRepository repository;

    NewOrderRequestHandler(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, FixMessage fixMessage, MarketClient marketClient) {
        this.channel = ctx.channel();
        new Thread(() -> processBuyRequest(fixMessage)).start();
    }

    private void processBuyRequest(FixMessage message)  {
        boolean isPurchased = purchase(message);
        if (isPurchased)
            sendBuySuccessResponse(message);
        else
            sendRejectResponse(message);
    }

    private void sendRejectResponse(FixMessage fixMessage) {
        //TODO reject message
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
        InstrumentModel instrument = repository.getByID(fixMessage.getSymbol(), InstrumentModel.class);
        BrokerUser user =  repository.getByID(fixMessage.getClientId(), BrokerUser.class);

        if (instrument == null || user == null) return false;

//        if (canInstrumentBeBoughtWithAmount(instrument, fixMessage.getPrice()))
//            return false;
//        if (!clientHasEnoughMoney(user, fixMessage.getPrice()))
//            return false;
//        if (!quantityEnoughAndAllowed( instrument, fixMessage.getOrderQuantity()))
//            return false;
        user.setAccountBalance(user.getAccountBalance() - fixMessage.getPrice());
        instrument.setQuantity(instrument.getQuantity() - fixMessage.getOrderQuantity());

        if (!repository.update(instrument)){
            return false;
        }
        return repository.update(user);
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
