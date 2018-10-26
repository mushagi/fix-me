package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.core.IMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.market.FixMessageValidator;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import java.util.UUID;
import java.util.logging.Logger;

public class NewOrderRequestHandler implements IMessageHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final MarketClient client;
    private final IRepository repository;
    private final FixMessage requestMessage;
    private final String rawFixMessage;

    private  String orderStatus;
    private String text;
    private final StringBuilder errorBuilder = new StringBuilder();
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
        if (FixMessageValidator.isValidateNewOrderSingle()){
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
        if (requestMessage.delay != 0 && requestMessage.delay < 10000) {
            logger.info("The response of this message is delayed by " +requestMessage.delay + " milliseconds");
            try {
                Thread.sleep(requestMessage.delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        if (marketCanBuyInstrumentAtCost() && marketCanBuyWithQuantity()) {
            orderStatus = "7";
            text = "Filled : Transaction a success";
        } else
            text = "Rejected : " + errorBuilder.toString();

        saveTransactionToDatabase();
    }

    private void processBuyRequest() {
        if (brokerCanBuyInstrumentAtCost() && brokerCanBuyWithQuantity()) {
            orderStatus = "7";
            text = "Filled : Transaction a success";
        } else
            text = "Rejected : " + errorBuilder.toString();

        saveTransactionToDatabase();
    }

    private boolean marketCanBuyWithQuantity() {
        int requestedQuantity = requestMessage.getOrderQuantity();

        boolean isQuantityAccepted = requestedQuantity > 0 && requestedQuantity < 20000;

        if (!isQuantityAccepted)
            errorBuilder.append("The quantity is not allowed");
        else
            instrument.quantity += requestedQuantity;

        return isQuantityAccepted;
    }

    private boolean brokerCanBuyWithQuantity() {
        int instrumentQuantity = instrument.quantity;
        int requestedQuantity = requestMessage.getOrderQuantity();

        boolean isQuantityAvailable = requestedQuantity < instrumentQuantity;

        if (!isQuantityAvailable)
            errorBuilder.append("The quantity is too high");
        else
            instrument.quantity -= requestedQuantity;

        return isQuantityAvailable;
    }

    private boolean marketCanBuyInstrumentAtCost() {
        double instrumentPrice = instrument.price;
        double requestedPrice = requestMessage.getPrice();

        boolean canBuy = requestedPrice >= instrumentPrice;

        if (!canBuy)
            errorBuilder.append("Can't buy, price is too low");
        return true;
    }

    private void saveTransactionToDatabase() {
        TradeTransaction tradeTransaction = new TradeTransaction();

        tradeTransaction.setClient(requestMessage.getClientId());
        tradeTransaction.setClientOrderId(UUID.fromString(requestMessage.getClOrderId()));
        tradeTransaction.setSide(requestMessage.getSide());
        tradeTransaction.setSymbol(instrument.getId());
        tradeTransaction.setOrderStatus(orderStatus);
        tradeTransaction.setText(text);
        tradeTransaction.setPrice(requestMessage.getPrice());
        tradeTransaction.setQuantity(requestMessage.getOrderQuantity());

        repository.create(tradeTransaction);
    }

    private boolean brokerCanBuyInstrumentAtCost() {
        double buyersPrice = requestMessage.getPrice();
        double currentInstrumentCost = instrument.getPrice();

        return buyersPrice >= currentInstrumentCost;
    }

    private void sendExecutionReport() {
        FixMessage responseFixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withMessageType("8")
                .withSide(requestMessage.getSide())
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