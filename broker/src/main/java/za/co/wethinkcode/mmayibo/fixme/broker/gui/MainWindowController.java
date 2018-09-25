package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.Set;

public class MainWindowController {
    private Set<String> stringSet;
    ObservableList observableList = FXCollections.observableArrayList();

    @FXML
    private ListView<String> marketListView;

    public void startUp(){
        stringSet.add("String 1");
        stringSet.add("String 2");
        stringSet.add("String 3");
        stringSet.add("String 4");
        observableList.setAll(stringSet);
        marketListView.setItems(observableList);
        marketListView.setCellFactory(listView -> new ListViewCell());
    }
}
