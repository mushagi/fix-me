package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;

import java.lang.reflect.InaccessibleObjectException;


public class MainWindowController {
    private ObservableList  observableInstruments = FXCollections.observableArrayList();;

    @FXML
    private ChoiceBox<?> instrumentDropDown;

    @FXML
    private ListView marketListView;
    private Broker broker;

    @FXML
    private Label instrumentDetailTextInLine;


    public void setUpStartUp(Broker broker) {
        this.broker = broker;

        instrumentDropDown.setItems(observableInstruments);
        instrumentDropDown.valueProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem instanceof Instrument)
            {
                Instrument item = (Instrument) newItem;
                instrumentDetailTextInLine.setText("Name " + item.getName() +"\n" + "Price "+ item.getPrice() );
            }
        });


        marketListView.setItems(broker.markets);
        marketListView.setCellFactory((Callback<ListView<MarketData>, MarketListItemController>) listView -> new MarketListItemController(MainWindowController.this));
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
                observableInstruments.add(instrument);
            instrumentDropDown.getSelectionModel().select(0);
        }
    }


}
