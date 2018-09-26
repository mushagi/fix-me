package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MainWindowController implements BrokerInterface{
    private Set<MarketData>marketSet = new HashSet<>();
    ObservableList observableList = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<?> instrumentDropDown;

    @FXML
    private ListView<MarketData> marketListView;
    private Broker broker;
    private ObservableList  observableInstruments = FXCollections.observableArrayList();;

    public void setUpStartUp(Broker broker) {
        this.broker = broker;

        marketListView.setItems(observableList);
        instrumentDropDown.setItems(observableInstruments);
        broker.setBrokerInterface(this);
        broker.requestMarkets();

    }

    @Override
    public void updateMarkets(Collection<MarketData> markets) {

        Platform.runLater(() -> {
            if (markets != null)
            {
                for (MarketData market: markets) {
                    if (!marketSet.contains(market))
                        marketSet.add(market);
                }
            }
            observableList.clear();
            observableList.setAll(marketSet);

            System.out.println(observableList.size());

            marketListView.setCellFactory(studentListView -> new MarketListItemController(this));
        });
    }

    void updateMarketPanel(MarketData markertData) {
        observableInstruments.clear();
        observableInstruments.add("Select an instrument");
        for (Instrument instrument: markertData.getInstruments())
            observableInstruments.add(instrument.getName());
        instrumentDropDown.getSelectionModel().select(0);
    }
}
