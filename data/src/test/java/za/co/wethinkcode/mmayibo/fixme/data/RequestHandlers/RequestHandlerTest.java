package za.co.wethinkcode.mmayibo.fixme.data.RequestHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction.CreateTransactionProcessor;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction.GetAllTransactionProcessor;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessageBuilder;
import za.co.wethinkcode.mmayibo.fixme.data.model.InstrumentModel;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.HibernateRepository;

class RequestHandlerTest {

    @Test
    void handleRequest() throws InterruptedException {
        IRepository repository = new HibernateRepository();
        GetAllTransactionProcessor transactionProcessor = new GetAllTransactionProcessor(null);
        CreateTransactionProcessor createTransactionProcessor = new CreateTransactionProcessor(null);
        String content = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InstrumentModel instrumentModel = new InstrumentModel();
            instrumentModel.setId("idfff");
            instrumentModel.name = "whatever bor";
            content = objectMapper.writeValueAsString(instrumentModel);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        FixMessage fixMessage = new FixMessageBuilder()
                .newFixMessage()
                .withDbTable("instrument")
                .withDbData(content)
                .withDbTransactionType("create")
                .getFixMessage();
        createTransactionProcessor.process(fixMessage);
        
        FixMessage response = transactionProcessor.process(fixMessage);
        String encoded = FixEncode.encode(response);
        System.out.println(encoded);
    }
}