package com.vtaveira.ui.util;

import com.vtaveira.EzyChatApp;
import com.vtaveira.ui.controller.CleanableController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class FXMLLoaderUtil {
  public Scene loadScene(String fxmlName) throws IOException {
    var loader = new FXMLLoader(EzyChatApp.class.getResource("fxml/" + fxmlName));
    Parent root = loader.load();
    var scene = new Scene(root);
    Object controller = loader.getController();
    if (controller instanceof CleanableController) scene.setUserData(controller);
    return scene;
  }

  public void changeScene(Stage stage, String fxmlPath) throws IOException {
    var oldScene = stage.getScene();
    if (oldScene != null && oldScene.getUserData() instanceof CleanableController cleanable) cleanable.cleanup();
    var scene = loadScene(fxmlPath);
    stage.setScene(scene);
  }
}
