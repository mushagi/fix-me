package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

class TransactionListCellFactory implements Callback<ListView<TradeTransaction>, ListCell<TradeTransaction>> {
    private final MainWindowController mainWindowController;

    public TransactionListCellFactory(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @Override
    public ListCell<TradeTransaction> call(ListView<TradeTransaction> param) {
        return new TransactionListItemController(mainWindowController);
    }
}
