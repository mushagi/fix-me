package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import za.co.wethinkcode.mmayibo.fixme.data.fixprotocol.FixMessage;

import java.io.IOException;

public class LoginRegisterController extends BrokerUI {

    @FXML
    private TextField userNameText;

    @FXML
    private TextField nameText;

    @FXML
    void signUp(ActionEvent event) {
        String userName = userNameText.getText();
        String name = nameText.getText();
        String dbData = "username:\"" + userName + "\"" +
                "name:\"" + name + "\"";
        authenticate(userName, dbData, "signup");
    }


    @FXML
    void signIn(ActionEvent event) {
        String userName = userNameText.getText();
        String dbData = "username:\"" + userName + "\"";
        authenticate(userName, dbData, "signin");
    }

    private void authenticate(String username, String dbData, String authRequestType) {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {


                FixMessage request = broker.authRequestMessage(dbData, "broker", authRequestType);
                FixMessage message = broker.sendMessageWaitForResponse(request);
                if (message.getAuthStatus().equals("success")) {
                    showMainWindow(username);
                }
                return null;
            }
        }).start();
    }
    private void showMainWindow(String username) throws InterruptedException {

        setSceneToMainWindowStage(stage, "Broker", "/fxml/main-window.fxml", username);
    }

    private void setSceneToMainWindowStage(Stage stage, String title, String fxmlResource, String username) throws InterruptedException {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
                Parent root = loader.load();

                this.stage.setUserData(loader.getController());
                if (stage.getScene() == null) {
                    stage.setScene(new Scene(root));
                    stage.setMinHeight(553);
                    stage.setMinWidth(775);
                }
                else {
                    stage.getScene().setRoot(root);
                    transitToAnotherScene(root);
                }
                stage.setTitle(title);
                BrokerUI controller = loader.getController();
                controller.setUpUi(broker, stage);
                this.unregisterFromBroker();
                broker.prepareBroker(username);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void transitToAnotherScene(Parent root)
    {
        FadeTransition fd = new FadeTransition();
        fd.setNode(root);
        fd.setFromValue(0);
        fd.setToValue(1);
        fd.setDuration(new Duration(1000));
        fd.setAutoReverse(true);
        fd.play();
    }

    @Override
    public void update() {

    }
}
