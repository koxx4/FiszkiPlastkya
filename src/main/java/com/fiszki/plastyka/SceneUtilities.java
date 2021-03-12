package com.fiszki.plastyka;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneUtilities
{
    public static void loadScene(URL path, Scene currentScene) throws IOException
    {
        Parent parent = null;
        FXMLLoader loader = new FXMLLoader(path);
        parent = loader.load();
        currentScene.setRoot(parent);
        Stage.getWindows().get(0).sizeToScene();
    }
}
