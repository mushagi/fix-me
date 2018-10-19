package za.co.wethinkcode.mmayibo.fixme.market.handlers;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.HibernateRepository;
import za.co.wethinkcode.mmayibo.fixme.data.model.TradeTransaction;

class NewOrderRequestHandlerTest {

    @Test
    void handleMessage() {
        HibernateRepository repository = new HibernateRepository();

        repository.create(new TradeTransaction());
    }
}