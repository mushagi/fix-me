package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;


public class MainWindowController implements BrokerInterface{
    ObservableList<MarketData> observableList = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<?> instrumentDropDown;

    @FXML
    private ListView<MarketData> marketListView;
    private Broker broker;
    private ObservableList  observableInstruments = FXCollections.observableArrayList();;

    public void setUpStartUp(Broker broker) {
        this.broker = broker;

        broker.setBrokerInterface(this);
        broker.requestMarkets();

    }

    @Override
    public void updateMarketSnapShot(MarketData marketData) {
        Platform.runLater(() -> {
            marketListView.getItems().add(marketData);
        });


    }

    void updateMarketPanel(MarketData marketData) {

        observableInstruments.add("Select an instrument");

        for (Instrument instrument: marketData.getInstruments())
            observableInstruments.add(instrument.getName());

        instrumentDropDown.getSelectionModel().select(0);
    }
}
