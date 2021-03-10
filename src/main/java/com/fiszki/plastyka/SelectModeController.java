package com.fiszki.plastyka;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SelectModeController
{
    @FXML
    AnchorPane mainPane;

    @FXML
    private void setArtStyleMode()
    {
        MainController.setCurrentGuessingMode(MainController.GuessingMode.STYLE);
        startGuessingScene();
    }
    @FXML
    private void setAuthorMode()
    {
        MainController.setCurrentGuessingMode(MainController.GuessingMode.AUTHOR);
        startGuessingScene();
    }
    @FXML
    private void setNameMode()
    {
        MainController.setCurrentGuessingMode(MainController.GuessingMode.NAME);
        startGuessingScene();
    }
    @FXML
    private void setPeriodMode()
    {
        MainController.setCurrentGuessingMode(MainController.GuessingMode.PERIOD);
        startGuessingScene();
    }
    private void startGuessingScene()
    {
        URL newSceneUrl = getClass().getResource("/MainScene.fxml");
        Parent parent = null;
        try
        {
            parent = FXMLLoader.load(newSceneUrl);
            mainPane.getScene().setRoot(parent);
            Stage.getWindows().get(0).sizeToScene();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
