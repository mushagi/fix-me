package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

class TransactionListItemController extends ListCell<TradeTransaction> {

    private final MainWindowController mainWindowController;
    static private Locale locale = new Locale("en", "za");
    static private NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
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
            status.setText("");
        }
        else {
            heading.setText("New order :  "+ item.getSide());
            status.setText((item.getOrderStatus().equals("7") ? "Success" : "Rejected"));
            status.setTextFill(item.getOrderStatus().equals("7") ? Color.GREEN : Color.RED);
            subheading.setText("Symbol : " + item.getSymbol());

            detailed.setText("Market bought from "+ item.getMarket() +
                    "\nPrice : "+ numberFormat.format(item.getPrice()) +
                    "\nQuantity " + item.getQuantity() +
                    "\nClient order : " + item.getClientOrderId() +
                    "\nText : " + item.getText());
        }
    }
}