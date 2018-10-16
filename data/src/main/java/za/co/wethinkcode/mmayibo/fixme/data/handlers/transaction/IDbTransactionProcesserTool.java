package za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction;

import za.co.wethinkcode.mmayibo.fixme.data.DataClient;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction.CreateTransactionProcessor;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction.GetAllTransactionProcessor;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction.GetTransactionProcessor;
import za.co.wethinkcode.mmayibo.fixme.data.handlers.transaction.IDbTransactionProcessor;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

public class IDbTransactionProcesserTool {

    public static IDbTransactionProcessor get(String dbTransactionType, DataClient dataClient) {
        switch (dbTransactionType)
        {
            case "create" :
                return new CreateTransactionProcessor(dataClient);
            case "getall" :
                return new GetAllTransactionProcessor(dataClient);
            case "getmultiple" :
                return new CreateTransactionProcessor(dataClient);
            case "get" :
                return new GetTransactionProcessor(dataClient);
            case "update" :
                return new CreateTransactionProcessor(dataClient);
            case "delete" :
                return new CreateTransactionProcessor(dataClient);
        }
        return null;
    }
}
