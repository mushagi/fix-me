package za.co.wethinkcode.mmayibo.fixme.data.handlers.messages;

import io.netty.channel.Channel;
import za.co.wethinkcode.mmayibo.fixme.data.DataClient;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.data.persistence.IRepository;

public class FixMessageHandlerTool {

    public static IFixMessageHandler get(FixMessage request, DataClient dataClient) {
        if (request.getMessageType() != null){
            switch (request.getMessageType().toLowerCase()){
                case "authrequest" :
                    return new AuthRequestHandler(request, dataClient);
                case "dbrequest" :
                    return new TransactionHandler(request, dataClient);
                case "1" :
                    return new IdResponseHandler(request, dataClient);
            }
        }
        return new InvalidResponseRequestHandler();
    }
}
