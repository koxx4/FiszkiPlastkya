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
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class GuessingSceneController implements Initializable
{
    public enum GuessingMode
    {
        STYLE, NAME, AUTHOR, PERIOD
    };
    private static final String[] easterEggs = new String[]{"Kocham Natalie <3", "Made by Piotr Wilk", "<3", "jak ci idzie Slonce?", "UwU", "OwO"};
    private static GuessingMode currentGuessingMode = GuessingMode.STYLE;
    @FXML
    private EditCardComponentController editCardComponentController;
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
        nextCardButton.setVisible(false);
        editCardButton.setVisible(false);
        //Reveal edit card component
        editCardComponentController.container.setVisible(true);

        editCardComponentController.startEdit(() -> {
            nextCardButton.setVisible(true);
            editCardButton.setVisible(true);
            editCardComponentController.container.setVisible(false);
        } );
    }
    @FXML
    public void showPreviousCard(ActionEvent event)
    {
        correctAnswerLabel.setText(easterEggs[FiszkiCardsModel.randomGenerator.nextInt(easterEggs.length)]);
        imageView.setImage(FiszkiCardsModel.getCardImage(FiszkiCardsModel.getRandomCard()));
    }
    @FXML
    public void showNextCard(ActionEvent event)
    {
        correctAnswerLabel.setText(easterEggs[FiszkiCardsModel.randomGenerator.nextInt(easterEggs.length)]);
        imageView.setImage(FiszkiCardsModel.getCardImage(FiszkiCardsModel.getRandomCard()));
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
        imageView.setImage(FiszkiCardsModel.getCardImage(FiszkiCardsModel.getRandomCard()));
        modeDescription.setText("Aktywny tryb: " + currentGuessingMode.toString());
        editCardButton.setTooltip( new Tooltip("Jezeli wydaje ci sie, ze dane w fiszce sa niepoprawne, mozesz ja edytowac."));
    }
    public static void setCurrentGuessingMode(GuessingMode mode)
    {
        GuessingSceneController.currentGuessingMode = mode;
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

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
