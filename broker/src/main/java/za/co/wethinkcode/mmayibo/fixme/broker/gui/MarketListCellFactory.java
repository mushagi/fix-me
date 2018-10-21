package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;

class MarketListCellFactory implements Callback<ListView<Market>, ListCell<Market>> {
    private final MainWindowController mainWindowController;

    MarketListCellFactory(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @Override
    public ListCell<Market> call(ListView<Market> param) {
        return new MarketListItemController(mainWindowController);
    }
}
