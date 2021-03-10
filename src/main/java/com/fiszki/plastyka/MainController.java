package com.fiszki.plastyka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class MainController implements Initializable
{
    public enum GuessingMode
    {
        STYLE, NAME, AUTHOR, PERIOD
    };
    private static final String[] easterEggs = new String[]{"Kocham Natalie <3", "Made by Piotr Wilk", "<3", "jak ci idzie Slonce?", "UwU", "OwO"};
    private static Random randomGenerator;
    private static GuessingMode currentGuessingMode;
    @FXML
    private EditCardComponentController editCardComponentController;
    @FXML
    private Button previousCardButton;
    @FXML
    private Button nextCardButton;
    @FXML
    private Button editCardButton;
    @FXML
    private TextField userAnswerField;
    @FXML
    private Label modeDescription;
    @FXML
    private Label correctAnswerLabel;
    @FXML
    private ImageView imageView;

    @FXML
    private void editCard()
    {
        //Hide buttons changing card
        previousCardButton.setVisible(false);
        nextCardButton.setVisible(false);
        editCardButton.setVisible(false);
        //Reveal edit card component
        editCardComponentController.container.setVisible(true);

        editCardComponentController.startEdit(() -> {
            previousCardButton.setVisible(true);
            nextCardButton.setVisible(true);
            editCardButton.setVisible(true);
            editCardComponentController.container.setVisible(false);
        } );
    }
    @FXML
    public void showPreviousImage(ActionEvent event)
    {
        correctAnswerLabel.setText(easterEggs[randomGenerator.nextInt(easterEggs.length)]);
        imageView.setImage(new Image(FiszkiCardsModel.getRandomNextArtImage().toString()));
    }
    @FXML
    public void showNextImage(ActionEvent event)
    {
        correctAnswerLabel.setText(easterEggs[randomGenerator.nextInt(easterEggs.length)]);
        imageView.setImage(new Image(FiszkiCardsModel.getRandomNextArtImage().toString()));
    }
    @FXML
    public void validateUserAnswer(ActionEvent event)
    {
        String userAnswer = userAnswerField.getText().toLowerCase();
        if(!userAnswer.isEmpty())
        {
            userAnswer.replaceAll("[.,]+", "");
            //validation logic
            switch (currentGuessingMode){
                case STYLE :  correctAnswerLabel.setText(FiszkiCardsModel.getActiveCard().style); break;
                case NAME :   correctAnswerLabel.setText(FiszkiCardsModel.getActiveCard().name); break;
                case AUTHOR : correctAnswerLabel.setText(FiszkiCardsModel.getActiveCard().author); break;
                case PERIOD : correctAnswerLabel.setText(FiszkiCardsModel.getActiveCard().period); break;
            }

            userAnswerField.clear();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //Hide edit card field in default
        editCardComponentController.container.setVisible(false);
        randomGenerator = new Random();
        FiszkiCardsModel.initializeModel();
        imageView.setImage(new Image(FiszkiCardsModel.getPrevArtImage().toString()));
        modeDescription.setText("Aktywny tryb: " + currentGuessingMode.toString());
        editCardButton.setTooltip( new Tooltip("Jezeli wydaje ci sie, ze dane w fiszce sa niepoprawne, mozesz ja edytowac."));
    }

    public static void setCurrentGuessingMode(GuessingMode mode)
    {
        MainController.currentGuessingMode = mode;
    }

    @FXML
    private void returnToModeSelect()
    {
        URL newSceneUrl = getClass().getResource("/SelectModeScene.fxml");
        Parent parent = null;
        try
        {
            parent = FXMLLoader.load(newSceneUrl);
            Scene scene = imageView.getScene();
            scene.setRoot(parent);
            Stage.getWindows().get(0).sizeToScene();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
