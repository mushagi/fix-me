package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import java.io.IOException;

public class TransactionListItemController extends ListCell<TradeTransaction> {

    private final MainWindowController mainWindowController;

    @FXML
    private Label name;
    private VBox box;

    TransactionListItemController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
        loadFXML();

    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction.fxml.fxml"));
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
        if (selected)
            mainWindowController.updateSelectedMarket();
    }

    @Override
    protected void updateItem(TradeTransaction item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            name.setText("");
        }

        else {
            name.setText(item.getText());
        }

    }
}