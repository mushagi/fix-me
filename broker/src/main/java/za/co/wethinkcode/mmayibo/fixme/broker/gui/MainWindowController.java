package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainWindowController implements BrokerInterface{
    private HashMap<String, MarketData> marketSet = new HashMap<>();
    ObservableList observableList = FXCollections.observableArrayList();

    @FXML
    private ListView<MarketData> marketListView;
    private Broker broker;

    public void setUpStartUp(Broker broker) {
        this.broker = broker;


        broker.setBrokerInterface(this);
        broker.requestMarkets();

    }

    @Override
    public void updateMarkets(Collection<MarketData> markets) {
        if (markets != null)
        {
            for (MarketData market: markets) {
                if (!marketSet.contains(market))
                    marketSet.add(market);
            }
        }
        observableList.setAll(marketSet);
        marketListView.setItems(observableList);
        marketListView.setCellFactory(studentListView -> new MarketListViewCell());

    }
}
