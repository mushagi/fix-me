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
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.BrokerUser;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginRegisterController extends BrokerUI {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @FXML
    private TextField userNameText;

    @FXML
    private TextField nameText;

    @FXML
    void signUp(ActionEvent event) {
        String userName = userNameText.getText();
        String name = nameText.getText();
        authenticate(userName, name);
    }


    @FXML
    void signIn(ActionEvent event) {
        String userName = userNameText.getText();
        String name = nameText.getText();
        authenticate(userName, name);
    }



    private void authenticate(final String username, final String name) {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                logger.info("Getting an existing account");
                BrokerUser brokerUser = broker.repository.getByID(username, BrokerUser.class);

                if (brokerUser == null)
                {
                    logger.info("Could not find the market on the database");
                    logger.info("Creating a new a market");
                    brokerUser = new BrokerUser(username, name);
                    broker.repository.create(brokerUser);
                    broker.user = brokerUser;
                }

                logger.info("Market "+ broker.user.getName()+" has been received");
                showMainWindow(username);
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
