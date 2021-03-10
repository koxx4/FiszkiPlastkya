package com.fiszki.plastyka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class Program extends Application
{
    public static void main(String[] args)
    {
        Program.launch(args);
    }
    @Override
    public void start(Stage stage)
    {
        Parent parent = null;
        try
        {
            parent = FXMLLoader.load(getClass().getResource("/MakeDatabase.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Scene mainScene = new Scene(parent);

        stage.setTitle("Fiszki plastyka!");
        URL icoPath  = getClass().getResource("/icon3.png");
        stage.getIcons().add(new Image(icoPath.toString()));
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.show();

    }
    @Override
    public void stop()
    {
        FiszkiCardsModel.closeModel();
    }
}


