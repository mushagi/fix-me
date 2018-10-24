package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import java.io.IOException;

class TransactionListItemController extends ListCell<TradeTransaction> {

    private final MainWindowController mainWindowController;

    @FXML
    private Label heading;

    @FXML
    private Label subheading;


    private Label detailed;

    @FXML
    private VBox box;

    TransactionListItemController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
        loadFXML();
        detailed = new Label();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
        Platform.runLater(() -> {
            if (selected)
                box.getChildren().add(2, detailed);
            else{
                box.getChildren().remove(detailed);
            }
        });


;

    }

    @Override
    protected void updateItem(TradeTransaction item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            heading.setText("");
            subheading.setText("");
            detailed.setText("");
        }

        else {
            String transactionHead = item.getSide().equals("buy") ? "Bought" : "Sold";
            heading.setText("Stock "+transactionHead+
                    "\tInstrument : " + item.getSymbol());
            subheading.setText("Price : "+ item.getPrice() + " \t"
                    + "Quantity " + item.getQuantity());
            detailed.setText("Market bought from"
                    + "\nClient order : " + item.getClientOrderId() +
                    "\nText : " + item.getText());

        }


    }
}