package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-window.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("Broker");
        primaryStage.setScene(new Scene(root, 1200, 700));

      //  MainWindowController mainWindowController = root.i
        primaryStage.show();


    }

    public static void main(String args[]) {
        launch(args);
    }

}
