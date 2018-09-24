package za.co.wethinkcode.mmayibo.fixme.broker;

import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixEncode;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessage;
import za.co.wethinkcode.mmayibo.fixme.core.fixprotocol.FixMessageBuilder;

import java.util.Scanner;

public class Main {
                    public static void main(String args[]) {
        Broker broker = new Broker("localhost", 5031);
        new Thread(broker).start();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            FixMessage fixMessage = new FixMessageBuilder()
                    .withMessageType("Sf")
                    .withBeginString("fsdf")
                    .withBodyLength("sada")
                    .withCheckSum("dsfs")
                    .withClOrderId("fsdf")
                    .withOrderQuantity("dsf").getFixMessage();

            String fixMessageString = FixEncode.encode(fixMessage);

            broker.sendMessage(fixMessageString);
        }
    }
}