package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.BrokerInstrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.Instrument;
import za.co.wethinkcode.mmayibo.fixme.core.model.MarketData;
import za.co.wethinkcode.mmayibo.fixme.core.model.Wallet;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class MainWindowController implements Initializable, BrokerUI{
    private ObservableList  observableInstruments = FXCollections.observableArrayList();;

    @FXML
    private ChoiceBox<?> instrumentDropDown;

    @FXML
    private ListView marketListView;

    @FXML
    private Label instrumentDetailTextInLine;

    @FXML
    private AreaChart<Number, Number> marketLineChart;

    private ObservableList<MarketData> markets;

    private MarketData selectedMarket;
    private BrokerInstrument selectedInstrument;

    private XYChart.Series<Number, Number> marketDataSeries = new XYChart.Series<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        marketLineChart.getData().add(marketDataSeries);

        instrumentDropDown.setItems(observableInstruments);

        instrumentDropDown.valueProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem instanceof Instrument) {

                selectedInstrument = (BrokerInstrument) newItem;

                resetLineGraphWithInstrumentHistory();
                updateUI();
            }
        });

        marketListView.setCellFactory((Callback<ListView<MarketData>, MarketListItemController>) listView -> new MarketListItemController(MainWindowController.this));
    }

    private void resetLineGraphWithInstrumentHistory() {
        Platform.runLater(() -> {
            if (selectedInstrument != null) {

                marketDataSeries.getData().clear();
                for (int i = 0; i < selectedInstrument.getPricesHistory().size(); i++)
                    marketDataSeries.getData().add(new XYChart.Data<>(i, selectedInstrument.getPricesHistory().get(i)));
            }
        });
    }


    @FXML
    void buyAction(ActionEvent event) {

    }
    void updateSelectedMarket() {

        Platform.runLater(() -> {
             selectedMarket = (MarketData) marketListView.getSelectionModel().getSelectedItem();

            if (selectedMarket != null && selectedMarket.getInstruments() != null) {
                observableInstruments.clear();
                observableInstruments.setAll(selectedMarket.getInstruments().values());
                instrumentDropDown.getSelectionModel().select(0);
            }
        });

    }

    @Override
    public void registerBroker(Broker broker) {
        markets = broker.markets;
        marketListView.setItems(markets);
        broker.register(this);
    }

    @Override
    public void update() {
        updateUI();
    }

    @Override
    public void updateWallet(Wallet wallet) {
        System.out.println(wallet.getAvailableFunds());
    }

    private void updateUI() {
        Platform.runLater(() -> {
            if (selectedInstrument != null){
                instrumentDetailTextInLine.setText("Name " + selectedInstrument.getName() +"\n" + "Price "+ selectedInstrument.getPrice());
                ArrayList<Double> pricesHistory = selectedInstrument.getPricesHistory();
                int size = pricesHistory.size()  - 1;
                Double firstValue = selectedInstrument.getPricesHistory().get(size);

                if (size >= 19){
                    marketDataSeries.getData().remove(0);
                    for (XYChart.Data<Number, Number> data: marketDataSeries.getData()) {
                        data.setXValue(data.getXValue().intValue() - 1);
                    }
                }
                marketDataSeries.getData().add(new XYChart.Data<>(size,firstValue));

            }
            else
                instrumentDetailTextInLine.setText("");

        });
    }


}