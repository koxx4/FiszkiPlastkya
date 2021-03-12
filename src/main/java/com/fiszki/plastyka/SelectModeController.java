package com.fiszki.plastyka;

import javafx.fxml.FXML;
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
        GuessingSceneController.setCurrentGuessingMode(GuessingSceneController.GuessingMode.STYLE);
        startGuessingScene();
    }
    @FXML
    private void setAuthorMode()
    {
        GuessingSceneController.setCurrentGuessingMode(GuessingSceneController.GuessingMode.AUTHOR);
        startGuessingScene();
    }
    @FXML
    private void setNameMode()
    {
        GuessingSceneController.setCurrentGuessingMode(GuessingSceneController.GuessingMode.NAME);
        startGuessingScene();
    }
    @FXML
    private void setPeriodMode()
    {
        GuessingSceneController.setCurrentGuessingMode(GuessingSceneController.GuessingMode.PERIOD);
        startGuessingScene();
    }
    private void startGuessingScene()
    {
        try
        {
            URL newSceneUrl = getClass().getResource("/GuessingScene.fxml");
            SceneUtilities.loadScene(newSceneUrl, mainPane.getScene());
            Stage.getWindows().get(0).sizeToScene();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToMainMenu()
    {
        try
        {
            SceneUtilities.loadScene(getClass().getResource("/MainMenuScene.fxml"), mainPane.getScene());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
