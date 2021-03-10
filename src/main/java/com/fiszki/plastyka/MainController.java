package com.fiszki.plastyka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Button previousImageButton;
    @FXML
    private Button nextImageButton;
    @FXML
    private TextField userAnswerField;
    @FXML
    private Label modeDescription;
    @FXML
    private Label correctAnswerLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView answerCheckIcon;

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
        randomGenerator = new Random();
        FiszkiCardsModel.initializeModel();
        imageView.setImage(new Image(FiszkiCardsModel.getPrevArtImage().toString()));

        modeDescription.setText("Aktywny tryb: " + currentGuessingMode.toString());
    }

    public static void setCurrentGuessingMode(GuessingMode mode)
    {
        MainController.currentGuessingMode = mode;
    }

}
