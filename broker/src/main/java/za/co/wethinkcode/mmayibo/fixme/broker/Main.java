package za.co.wethinkcode.mmayibo.fixme.broker;

import javafx.application.Application;
import za.co.wethinkcode.mmayibo.fixme.broker.gui.GuiMain;

public class Main {

    public static void main(String args[]) {
        Broker broker = new Broker("localhost", 5031);
        new Thread(broker).start();
        Application.launch(GuiMain.class, args);

    }
}