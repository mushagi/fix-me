package za.co.wethinkcode.mmayibo.fixme.broker.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MarketListItemController
{
    @FXML
    private AnchorPane root;
    @FXML
    private Label name;

    public MarketListItemController()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/marketlistitem.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(String string)
    {
        name.setText(string);
    }

    public AnchorPane getBox()
    {
        return root;
    }
}