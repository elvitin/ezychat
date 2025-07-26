package com.vtaveira.ui.controller;


import com.vtaveira.util.ResourcesLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class MainChatController implements Initializable {

  @FXML
  private VBox chatContainer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.setupBackground();
  }

  private void setupBackground() {
    var backgroundImage = new BackgroundImage(
        ResourcesLoader.loadImageFromAssets("background.png"),
        BackgroundRepeat.REPEAT,
        BackgroundRepeat.REPEAT,
        BackgroundPosition.DEFAULT,
        BackgroundSize.DEFAULT
    );
    chatContainer.setBackground(new Background(backgroundImage));
  }
}
