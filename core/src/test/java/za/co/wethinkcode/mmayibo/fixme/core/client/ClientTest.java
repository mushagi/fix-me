package za.co.wethinkcode.mmayibo.fixme.core.client;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ClientTest {

    public static void main(String args[]) throws InterruptedException, ExecutionException, IOException {
        Scanner scanner = new Scanner(System.in);
        AbstractClient client = new AbstractClient("localhost", 5003) {};

        while (true)
        {
            String string = scanner.nextLine();
            client.sendMessage(string);
        }

    }

}
