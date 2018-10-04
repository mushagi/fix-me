package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;

public class Gui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Broker broker = new Broker("localhost", 5001);
        new Thread(broker).start();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-window.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Broker");
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();

        MainWindowController controller = loader.getController();

        controller.registerBroker(broker);
    }

    public static void main(String args[]) {
        launch(args);
    }

}
