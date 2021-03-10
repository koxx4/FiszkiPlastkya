package com.fiszki.plastyka;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class EditCardComponentController
{
    @FXML TextField nameTextField;
    @FXML TextField authorTextField;
    @FXML TextField periodTextField;
    @FXML TextField styleTextField;
    @FXML Button confirmEditButton;
    @FXML Button cancelEditButton;
    @FXML VBox container;

    private IOnCardEditFinish onCardEditFinishCallback;
    private FiszkaCard cardToEdit;

    @FXML
    private void saveUserEdition()
    {
        cardToEdit.name = nameTextField.getText().isBlank() ? "nieznane" : nameTextField.getText();
        cardToEdit.author = authorTextField.getText().isBlank() ? "nieznany" : nameTextField.getText();
        cardToEdit.period = periodTextField.getText().isBlank() ? "nieznany" : nameTextField.getText();
        cardToEdit.style = styleTextField.getText().isBlank() ? "nieznany" : nameTextField.getText();
        //Cleanup after user input
        endUserEdition();
    }
    @FXML
    private void endUserEdition()
    {
        nameTextField.clear();
        authorTextField.clear();
        styleTextField.clear();
        periodTextField.clear();
        cardToEdit = null; //To avoid unexpected edit in future
        onCardEditFinishCallback.onCardEditFinishAction();
    }
    @FXML
    public void startEdit(IOnCardEditFinish onCardEditFinishCallback)
    {
        cardToEdit = FiszkiCardsModel.getActiveCard();
        nameTextField.setText(cardToEdit.name);
        authorTextField.setText(cardToEdit.author);
        styleTextField.setText(cardToEdit.style);
        periodTextField.setText(cardToEdit.period);
        this.onCardEditFinishCallback = onCardEditFinishCallback;
    }

}
