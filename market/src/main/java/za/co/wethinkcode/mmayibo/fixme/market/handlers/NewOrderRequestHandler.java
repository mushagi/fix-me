package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.market.FixMessageValidator;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import java.util.logging.Logger;

public class NewOrderRequestHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final MarketClient client;
    private final IRepository repository;
    private final FixMessage requestMessage;
    private final String rawFixMessage;

    private  String orderStatus;
    private String text;
    private StringBuilder errorBuilder = new StringBuilder();
    private InstrumentModel instrument;

    NewOrderRequestHandler(FixMessage requestMessage, MarketClient client, String rawFixMessage) {
        this.requestMessage = requestMessage;
        this.repository = client.repository;
        this.client = client;
        this.rawFixMessage = rawFixMessage;
    }
    @Override
    public void processMessage() {
        logger.info("Fix message read : " + rawFixMessage);
        new Thread(this::processRequest).start();
    }



    private void processRequest()  {
        orderStatus = "2";

        if (FixMessageValidator.isValidateNewOrderSingle(requestMessage, errorBuilder)){
            instrument =
                    client.marketModel.instrumentMao.get(requestMessage.getSymbol());

            if (instrument != null)
                processSideRequest();
            else
                text = "Rejected : Could not find the requested instrument";
        }
        else
            text = errorBuilder.toString();

        logger.info("Execution report of " + requestMessage.getClOrderId()
                + ". Result : " + orderStatus +  ". Text = {" + text + "}");

        sendExecutionReport();
    }

    private void processSideRequest() {
        String side = requestMessage.getSide().toLowerCase();

        switch (side) {
            case "buy":
                processBuyRequest();
                break;
            case "sell":
                processSellRequest();
                break;
            default:
                text = "invalid side request";
                break;
        }
    }

    private void processSellRequest() {
        if (marketBuyInstrumentAtCost()) {
            orderStatus = "7";
            text = "Filled : Transaction a success";
        } else
            text = "Rejected : " + errorBuilder.toString();

        saveTransactionToDatabase();
    }

    private void processBuyRequest() {
        if (marketSellInstrumentAtCost() && canBuyWithQuantity()) {
            orderStatus = "7";
            text = "Filled : Transaction a success";
        } else
            text = "Rejected : " + errorBuilder.toString();

        saveTransactionToDatabase();
    }

    private boolean canBuyWithQuantity() {
        int instrumentQuantity = instrument.quantity;
        int requestedQuantity = requestMessage.getOrderQuantity();

        boolean isQuantityAvailable = requestedQuantity < instrumentQuantity;
        instrument.quantity -= requestedQuantity;

        if (!isQuantityAvailable)
            text = "The quantity is too high";

        return isQuantityAvailable;
    }

    private boolean marketBuyInstrumentAtCost() {
        double instrumentPrice = instrument.price;
        double requestedPrice = requestMessage.getPrice();

        boolean canBuy = requestedPrice < instrumentPrice;

        if (!canBuy)
            text = "Can't buy, price is too low";
        return true;
    }

    private void saveTransactionToDatabase() {
        TradeTransaction tradeTransaction = new TradeTransaction();

        tradeTransaction.setClient(requestMessage.getClientId());
        //tradeTransaction.setClientOrderId(requestMessage.getClOrderId());
        tradeTransaction.setSide(requestMessage.getSide());
        tradeTransaction.setSymbol(instrument.getId());
        tradeTransaction.setOrderStatus(orderStatus);
        tradeTransaction.setText(text);
        tradeTransaction.setPrice(requestMessage.getPrice());
        tradeTransaction.setQuantity(requestMessage.getOrderQuantity());

        repository.create(tradeTransaction);
    }

    private boolean marketSellInstrumentAtCost() {
        double buyersPrice = requestMessage.getPrice();
        double currentInstrumentCost = instrument.getPrice();

        return buyersPrice >= currentInstrumentCost;
    }

    private void sendExecutionReport() {
        FixMessage responseFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("8")
                .withMessageId(requestMessage.getMessageId())
                .withClientIId(requestMessage.getClientId())
                .withClOrderId(requestMessage.getClOrderId())
                .withOrdStatus(orderStatus)
                .withSymbol(requestMessage.getSymbol())
                .withText(text)
                .withTargetCompId(requestMessage.getSenderCompId())
                .withSenderCompId(requestMessage.getTargetCompId())
                .getFixMessage();

        client.sendResponse(responseFixMessage);
    }
}