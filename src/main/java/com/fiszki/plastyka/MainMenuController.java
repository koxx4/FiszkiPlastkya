package com.fiszki.plastyka;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable
{
    @FXML private Button downloadCardsButton;
    @FXML private Button downloadImagesButton;
    @FXML private Button makeCardsButton;
    @FXML private Button startButton;
    @FXML private Button exitButton;
    @FXML private Label progressLabel;
    @FXML private ProgressBar progressBar;

    @FXML
    public void downloadCards(ActionEvent event)
    {
        downloadCardsButton.setVisible(false);
        makeCardsButton.setVisible(false);
        exitButton.setVisible(false);
        downloadImagesButton.setVisible(false);
        startButton.setVisible(false);
        progressBar.setVisible(true);
        progressLabel.setVisible(true);
        progressBar.setProgress(0);

        try
        {
            Task<Void> downloadingTask = new Task<Void>(){
                @Override
                public Void call() throws Exception
                {
                    FiszkiCardsModel.downloadOnlineCards(((fileName, progress) -> {
                        String progressString = String.format("%.02f", progress*100);
                        updateMessage("Downloading " + fileName + ". Progress: " + progressString + "%");
                        updateProgress(progress,1);
                    }));
                    return null;
                }
            };

            progressLabel.textProperty().bind(downloadingTask.messageProperty());
            progressBar.progressProperty().bind(downloadingTask.progressProperty());

            downloadingTask.setOnSucceeded(e -> {
                progressLabel.textProperty().unbind();
                progressBar.progressProperty().unbind();
                progressLabel.setText("operation completed successfully");
                downloadCardsButton.setVisible(true);
                makeCardsButton.setVisible(true);
                startButton.setVisible(true);
                exitButton.setVisible(true);
                downloadImagesButton.setVisible(true);
                progressBar.setVisible(false);
                progressLabel.setVisible(false);
            });

            Thread workingThread = new Thread(downloadingTask);
            workingThread.setDaemon(true);
            workingThread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
    @FXML
    private void downloadCardsImages()
    {
        downloadCardsButton.setVisible(false);
        makeCardsButton.setVisible(false);
        exitButton.setVisible(false);
        downloadImagesButton.setVisible(false);
        startButton.setVisible(false);
        progressBar.setVisible(true);
        progressLabel.setVisible(true);
        progressBar.setProgress(0);

        try
        {
            Task<Void> downloadingTask = new Task<Void>(){
                @Override
                public Void call()
                {
                    FiszkiCardsModel.downloadCardsImages(((fileName, progress) -> {
                        String progressString = String.format("%.02f", progress*100);
                        updateMessage("Downloading " + fileName + ". Progress: " + progressString + "%");
                        updateProgress(progress, 1);
                    }));
                    return null;
                }
            };
            progressLabel.textProperty().bind(downloadingTask.messageProperty());
            progressBar.progressProperty().bind(downloadingTask.progressProperty());
            downloadingTask.setOnSucceeded(e -> {
                progressLabel.textProperty().unbind();
                progressBar.progressProperty().unbind();
                // this message will be seen.
                progressLabel.setText("operation completed successfully");
                downloadCardsButton.setVisible(true);
                makeCardsButton.setVisible(true);
                startButton.setVisible(true);
                exitButton.setVisible(true);
                downloadImagesButton.setVisible(true);
                progressBar.setVisible(false);
                progressLabel.setVisible(false);
            });

            Thread workingThread = new Thread(downloadingTask);
            workingThread.setDaemon(true);
            workingThread.start();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        progressBar.setVisible(false);
        progressLabel.setVisible(false);
        try
        {
            FiszkiCardsModel.initializeModel();
        } catch (NoCardsLoadedException e)
        {
            progressLabel.setVisible(true);
            startButton.setVisible(false);
            progressLabel.setText("Brak fiszek!");
            e.printStackTrace();
        }
    }
    @FXML
    private void loadSelectModeScene()
    {
        //Make dirs for further data
        FiszkiCardsModel.makeDataDirs();
        try
        {
            FiszkiCardsModel.initializeModel();
        }catch (NoCardsLoadedException e)
        {
            progressLabel.setVisible(true);
            progressLabel.setText(e.getMessage()+"\n Nie wczytano zadnych fiszek!");
            return;
        }

        try
        {
            SceneUtilities.loadScene(getClass().getResource("/SelectModeScene.fxml"), startButton.getScene());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadCardMakerScene()
    {
        try
        {
            SceneUtilities.loadScene(getClass().getResource("/CardMakerScene.fxml"), startButton.getScene());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    private void exitApplication()
    {
        System.exit(0);
    }
}
