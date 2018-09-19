package za.co.wethinkcode.mmayibo.fixme.broker;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Broker broker = new Broker("localhost", 5031);
        new Thread(broker).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            broker.sendMessage(input +  "|" + "message");
        }
    }
}
