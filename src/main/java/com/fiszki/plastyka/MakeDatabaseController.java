package com.fiszki.plastyka;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MakeDatabaseController implements Initializable
{
    @FXML
    private Button cancelDownloadButton;
    @FXML
    private Button confirmDownloadButton;
    @FXML
    private Label infoLabel;

    @FXML
    public void confirmMakeDataBase(ActionEvent event)
    {
        cancelDownloadButton.setVisible(false);
        confirmDownloadButton.setVisible(false);
        try
        {
            Task<Void> downloadingTask = new Task<Void>(){
                @Override
                public Void call()
                {
                    FiszkiCardsModel.constructCards(((fileName, progress) -> {
                        String progressString = String.format("%.02f", progress*100);
                        updateMessage("Downloading " + fileName + ". Progress: " + progressString + "%");
                    }));
                    return null;
                }
            };
            infoLabel.textProperty().bind(downloadingTask.messageProperty());

            downloadingTask.setOnSucceeded(e -> {
                infoLabel.textProperty().unbind();
                // this message will be seen.
                infoLabel.setText("operation completed successfully");
                LoadMainScene();
            });

           Thread workingThread = new Thread(downloadingTask);
           workingThread.setDaemon(true);
           workingThread.start();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @FXML
    public void CancelDownload(ActionEvent event)
    {
        LoadMainScene();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        File fiszkiImages = new File(FiszkiCardsModel.IMAGES_PATH);
        var images = fiszkiImages.listFiles( (x,z) -> {
            return z.endsWith(".jpg") && z.startsWith("image");
        } );

        int downloadedImages = 0;

        if(images != null)
        {
            downloadedImages = images.length;
        }

        infoLabel.setText("Czy chcesz pobrac zdjecia fiszek? Ta czynnosc " +
                "powinna byc wykonana tylko przed pierwszym uzyciem programu. " +
                "Wykrytych zdjec do fiszek: " + downloadedImages);
    }
    private void LoadMainScene()
    {
        //Init model
        FiszkiCardsModel.initializeModel();
        URL newSceneUrl = getClass().getResource("/SelectModeScene.fxml");
        Parent parent = null;
        try
        {
            parent = FXMLLoader.load(newSceneUrl);
            infoLabel.getScene().setRoot(parent);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
