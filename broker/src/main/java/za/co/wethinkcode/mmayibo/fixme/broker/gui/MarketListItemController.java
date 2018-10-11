package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;

import java.io.IOException;

public class MarketListItemController extends ListCell<MarketModel> {

    private final MainWindowController mainWindowController;

    @FXML
    private Label name;

    private MarketModel markertData;
    private VBox box;

    MarketListItemController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
        loadFXML();

    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/marketlistitem.fxml"));
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
    protected void updateItem(MarketModel item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            name.setText("");
        }

        else {
            name.setText(item.getName());
        }

    }
}