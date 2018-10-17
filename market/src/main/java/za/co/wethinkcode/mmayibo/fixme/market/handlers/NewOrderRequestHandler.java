package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;
import za.co.wethinkcode.mmayibo.fixme.market.model.TradeTransaction;

import java.util.logging.Logger;

public class NewOrderRequestHandler implements FixMessageHandlerResponse {
    private Logger logger = Logger.getLogger(getClass().getName());
    private final MarketClient client;
    private IRepository repository;

    NewOrderRequestHandler(MarketClient client) {
        this.repository = client.repository;
        this.client = client;
    }

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage message) {
        new Thread(() -> processBuyRequest(message)).start();
    }

    private void processBuyRequest(FixMessage message)  {
        boolean isPurchased = purchase(message);
        if (isPurchased)
            sendBuySuccessResponse(message);
        else
            sendRejectResponse(message);
    }

    private boolean purchase(FixMessage fixMessage) {
        InstrumentModel instrument =
                client.marketModel.instrumentsHashMap.get(fixMessage.getSymbol());

        if (instrument == null)
        {
            logger.info("Rejected : Could not find the requested instrument");
            return false;
        }
        if (!canInstrumentBeBoughtWithAmount(instrument, fixMessage.getPrice())) {
            logger.info("Rejected : The price of the instrument is higher");
            return false;
        }

        TradeTransaction tradeTransaction = new TradeTransaction();
        repository.create(tradeTransaction);
        logger.info("Success : Transaction a success");

        return true;
    }

    private boolean canInstrumentBeBoughtWithAmount(InstrumentModel instrument, double price) {
        return price >= instrument.getPrice();
    }

    private void sendRejectResponse(FixMessage message) {
        FixMessage rejectMessage = new FixMessageBuilder()
                .newFixMessage()
                .withRefMsgType(message.getMessageType())
                .withMessageType("3")
                .withMessage("")
                .withSymbol(message.getSymbol())
                .withTargetCompId(message.getSenderCompId())
                .withSenderCompId(message.getTargetCompId())
                .withMessageId(message.getMessageId())
                .getFixMessage();
        client.sendResponse(rejectMessage);
    }


    private void sendBuySuccessResponse(FixMessage fixMessage) {
        FixMessage responseFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("")
                .withSymbol(fixMessage.getSymbol())
                .withTargetCompId(fixMessage.getSenderCompId())
                .withSenderCompId(fixMessage.getTargetCompId())
                .getFixMessage();
        client.sendResponse(responseFixMessage);
    }
}
