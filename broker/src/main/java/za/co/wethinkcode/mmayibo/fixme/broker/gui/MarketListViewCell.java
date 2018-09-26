package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.scene.control.ListCell;

public class MarketListViewCell extends ListCell<MarketData>
{
    @Override
    public void updateItem(MarketData marketData, boolean empty)
    {
        super.updateItem(marketData ,empty);
        if(marketData != null)
        {
            MarketListItemController marketListItemController = new MarketListItemController();
            marketListItemController.setInfo(marketData.getId());
            setGraphic(marketListItemController.getBox());
        }
    }
}