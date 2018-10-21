package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import za.co.wethinkcode.mmayibo.fixme.broker.Broker;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Instrument;
import za.co.wethinkcode.mmayibo.fixme.broker.model.domain.Market;
import za.co.wethinkcode.mmayibo.fixme.core.model.TradeTransaction;

import javax.swing.text.GlyphView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;


public class MainWindowController extends BrokerUI implements Initializable {
    private final ObservableList<Instrument>  observableInstruments = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<Instrument> instrumentDropDown;

    @FXML
    private ListView<Market> marketListView;

    @FXML
    private ListView<TradeTransaction> transactionListView;

    @FXML
    private Label instrumentDetailTextInLine;

    @FXML
    private AreaChart<Number, Number> marketLineChart;

    private Market selectedMarket;
    private Instrument selectedInstrument;
    ObservableMap<UUID, TradeTransaction> observableTransactions;

    private final XYChart.Series<Number, Number> marketDataSeries = new XYChart.Series<>();

    @FXML
    private TextField quantityText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        marketLineChart.getData().add(marketDataSeries);

        instrumentDropDown.setItems(observableInstruments);

        instrumentDropDown.valueProperty().addListener((obs, oldItem, newItem) -> {
                selectedInstrument =  newItem;
                resetLineGraphWithInstrumentHistory();
                update();
        });
        marketListView.setCellFactory(new MarketListCellFactory(this));
        transactionListView.setCellFactory(new TransactionListCellFactory(this));
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
    void buyAction(ActionEvent event){

        if (selectedInstrument != null){
            try{
                int quantity = Integer.parseInt(quantityText.getText());
                broker.newOrderSingle(selectedMarket, selectedInstrument, "buy", quantity);
            }catch (NumberFormatException e) {

            }
        }
    }
    void updateSelectedMarket() {
        Platform.runLater(() -> {
            selectedMarket = marketListView.getSelectionModel().getSelectedItem();

            if (selectedMarket != null && selectedMarket.getInstruments() != null) {
                observableInstruments.clear();
                observableInstruments.setAll(selectedMarket.getInstruments().values());
                instrumentDropDown.getSelectionModel().select(0);
            }
        });
    }

    @Override
    void setUpUi(Broker broker, Stage stage) {
        super.setUpUi(broker, stage);

        ObservableMap<String, Market> observableMarkets = FXCollections.observableMap(markets);

        observableTransactions = FXCollections.observableMap(transactions);

        marketListView.getItems().setAll(observableMarkets.values());
        transactionListView.getItems().setAll(observableTransactions.values());
        observableTransactions.addListener((MapChangeListener<UUID, TradeTransaction>) change -> {

            if (change.wasAdded()) {
                transactionListView.getItems().add(change.getValueAdded());
            }
            else if (change.wasRemoved())
                transactionListView.getItems().remove(change.getValueRemoved());

        });


    }

    @Override
    public void update() {
        Platform.runLater(() -> {
            if (selectedInstrument != null){
                instrumentDetailTextInLine.setText("Name " + selectedInstrument.getName() +"\n" + "Price "+ selectedInstrument.getCostPrice());
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

    @FXML
    public void sellAction(ActionEvent actionEvent) {
        int quantity = Integer.parseInt(quantityText.getText());
        if (selectedInstrument != null)
            broker.newOrderSingle(selectedMarket, selectedInstrument, "sell", quantity);
    }

    @Override
    public void updateTransactions(final TradeTransaction tradeTransaction){
        Platform.runLater(() -> {
            observableTransactions.put(tradeTransaction.getClientOrderId(), tradeTransaction);
            System.out.println("transaction done");
        });
    }
}