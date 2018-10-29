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

    @FXML
    private Label status;

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
        System.out.println("selected");
        Platform.runLater(() -> {
            if (selected)
                box.getChildren().add(2, detailed);
            else{
                box.getChildren().remove(detailed);
            }
        });
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
            heading.setText("Stock "+transactionHead);
            status.setText("Status : " + (item.getOrderStatus().equals("7") ? "Success" : "Rejected"));
            subheading.setText("Symbol : " + item.getSymbol());

            detailed.setText("Market bought from"+
                    "Price : "+ item.getPrice() + " \t"
                    + "Quantity " + item.getQuantity() +
                    "\nClient order : " + item.getClientOrderId() +
                    "\nText : " + item.getText());
        }


    }
}