package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;


public class MainWindowController implements BrokerInterface{
    ObservableList observableList = FXCollections.observableArrayList();
    private ObservableList  observableInstruments = FXCollections.observableArrayList();;

    @FXML
    private ChoiceBox<?> instrumentDropDown;

    @FXML
    private ListView marketListView;
    private Broker broker;

    public void setUpStartUp(Broker broker) {
        this.broker = broker;

        broker.setBrokerInterface(this);
        broker.requestMarkets();

        instrumentDropDown.setItems(observableInstruments);
        marketListView.setItems(observableList);
        marketListView.setCellFactory(new Callback<ListView<MarketData>, MarketListItemController>()
        {
            @Override
            public MarketListItemController call(ListView<MarketData> listView)
            {
                return new MarketListItemController(MainWindowController.this);
            }
        });
    }

    @Override
    public void updateMarketSnapShot(MarketData marketData) {
        if (!observableList.contains(marketData))
            observableList.add(marketData);
    }

    @FXML
    void buyAction(ActionEvent event) {


    }


    void updateMarketPanel(MarketData marketData) {
        observableInstruments.clear();
        observableInstruments.add("Select an instrument");

        if (marketData != null && marketData.getInstruments() != null)
        {
            for (Instrument instrument: marketData.getInstruments())
                observableInstruments.add(instrument.getName());
            instrumentDropDown.getSelectionModel().select(0);
        }

    }
}
