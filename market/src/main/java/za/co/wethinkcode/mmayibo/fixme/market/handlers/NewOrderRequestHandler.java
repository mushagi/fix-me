package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageHandler;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.market.FixMessageValidator;
import za.co.wethinkcode.mmayibo.fixme.market.MarketClient;
import za.co.wethinkcode.mmayibo.fixme.market.model.TradeTransaction;

import java.util.logging.Logger;

public class NewOrderRequestHandler implements FixMessageHandlerResponse {
    private Logger logger = Logger.getLogger(getClass().getName());

    private final MarketClient client;

    private IRepository repository;

    private String orderStatus;
    private FixMessage requestMessage;
    private String text;
    private StringBuilder errorBuilder = new StringBuilder();
    private InstrumentModel instrument;

    NewOrderRequestHandler(MarketClient client) {
        this.repository = client.repository;
        this.client = client;
    }

    @Override
    public void next(FixMessageHandler next) {

    }

    @Override
    public void handleMessage(FixMessage requestMessage) {
        this.requestMessage = requestMessage;
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
        if (marketSellInstrumentAtCost()) {
            orderStatus = "7";
            text = "Filled : Transaction a success";
        } else
            text = "Rejected : " + errorBuilder.toString();

        saveTransactionToDatabase();
    }

    private boolean marketBuyInstrumentAtCost() {
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