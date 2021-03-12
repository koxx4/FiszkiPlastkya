package com.fiszki.plastyka;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardMakerController implements Initializable
{
    @FXML TextField idTextField;
    @FXML TextField imageURLTextField;
    @FXML TextField nameTextField;
    @FXML TextField authorTextField;
    @FXML TextField styleTextField;
    @FXML TextField periodTextField;
    @FXML Label warningLabel;
    @FXML ImageView previewImage;

    @FXML
    private void saveCard()
    {
        FiszkaCard newCard = new FiszkaCard();
        String newCardID = idTextField.getText();
        if(!FiszkiCardsModel.isValidID(newCardID))
        {
            warningLabel.setVisible(true);
            warningLabel.setText("\'"+newCardID+"\'" + " to nieprawidlowe ID. Sprobuj z innym!");
            return;
        }
        newCard.id = newCardID;
        newCard.name = nameTextField.getText();
        newCard.author = authorTextField.getText();
        newCard.style = styleTextField.getText();
        newCard.period = periodTextField.getText();
        newCard.imageURL = imageURLTextField.getText();
        FiszkiCardsModel.addNewCard(newCard);
        nameTextField.clear();
        authorTextField.clear();
        styleTextField.clear();
        periodTextField.clear();
        imageURLTextField.clear();
        idTextField.clear();
        tryLoadPreviewImage(false);
        warningLabel.setVisible(true);
    }
    @FXML
    private void returnToMainMenu()
    {
        try
        {
            SceneUtilities.loadScene(getClass().getResource("/MainMenuScene.fxml"), warningLabel.getScene());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    private void tryLoadPreviewImage(boolean isFocused)
    {
        if(!isFocused)
        {
            try
            {
                URL imageURL = new URL(imageURLTextField.getText());
                Image downloadedPreviewImage = new Image(String.valueOf(imageURL));
                previewImage.setImage(downloadedPreviewImage);
            } catch (Exception e)
            {
                previewImage.setImage(FiszkiCardsModel.getBlankImage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        warningLabel.setVisible(false);
        imageURLTextField.focusedProperty().addListener((obs, oldVal, newVal) ->
                tryLoadPreviewImage(newVal));
    }
}
