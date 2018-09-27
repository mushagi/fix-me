package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;


public class MainWindowController implements BrokerInterface{
    ObservableList observableList = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<?> instrumentDropDown;

    @FXML
    private ListView<String> marketListView;
    private Broker broker;
    private ObservableList  observableInstruments = FXCollections.observableArrayList();;

    public void setUpStartUp(Broker broker) {
        this.broker = broker;

        broker.setBrokerInterface(this);
        broker.requestMarkets();
        Platform.runLater(() -> {
            observableInstruments.add(new MarketData("Fsddasdasfsd", "Sdgsd"));
            observableInstruments.add(new MarketData("Fsdfsd", "Sdgasdasdsd"));
            observableInstruments.add(new MarketData("Fsdfsd", "Sdgsadasdasdsd"));
            observableInstruments.add(new MarketData("Fsdfsd", "Sdgasdasddsadsdssd"));

            marketListView.setItems(observableList);
            instrumentDropDown.setItems(observableInstruments);

            marketListView.setCellFactory(studentListView -> new MarketListItemController(this));
        });

    }

    @Override
    public void updateMarketSnapShot(MarketData marketData) {
        observableInstruments.add(marketData);
    }

    void updateMarketPanel(MarketData marketData) {

        observableInstruments.add("Select an instrument");

        for (Instrument instrument: marketData.getInstruments())
            observableInstruments.add(instrument.getName());

        instrumentDropDown.getSelectionModel().select(0);
    }
}
