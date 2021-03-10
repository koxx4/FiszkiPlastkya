package com.fiszki.plastyka;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class EditCardComponent /*which is for now also a controller*/ extends VBox
{

    public EditCardComponent()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CardEditComponent.fxml"));
            fxmlLoader.setController(new EditCardComponentController());
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
