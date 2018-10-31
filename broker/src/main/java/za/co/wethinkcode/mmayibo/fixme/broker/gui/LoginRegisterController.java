package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
    import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.BrokerUser;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginRegisterController extends BrokerUI {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @FXML
    private TextField userNameText;

    @FXML
    void signIn() {
        String userName = userNameText.getText();
        authenticate(userName);
    }



    private void authenticate(final String username) {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                logger.info("Getting an existing account");
                BrokerUser user = broker.repository.getByID(username, BrokerUser.class);

                if (user == null)
                {
                    logger.info("Could not find the market on the database");
                    logger.info("Creating a new a market");
                    user = new BrokerUser(username);
                    user.setNetworkId(-1);
                    broker.repository.create(user);
                    broker.user = user;
                }
                else
                    broker.networkId = String.valueOf(user.getNetworkId());
                broker.user = user;
                logger.info("Market "+ broker.user.getUserName()+" has been received");
                showMainWindow();
                return null;
            }
        }).start();
    }
    private void showMainWindow() {
        setSceneToMainWindowStage(stage, "Broker", "/fxml/main-window.fxml");
    }

    private void setSceneToMainWindowStage(Stage stage, String title, String fxmlResource) {
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
                stage.setOnCloseRequest(event -> controller.onClose());

                this.unregisterFromBroker();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void transitToAnotherScene(Parent root) {
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

    @Override
    public void updateMarkets(Market market, boolean wasOnlineStatusChanged) {

    }

    @Override
    public void updateTransactions(TradeTransaction tradeTransaction) {

    }

}
