package com.vtaveira.ezychat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EzyChatAppController {
  @FXML
  private Label welcomeText;

  @FXML
  protected void onHelloButtonClick() {
    welcomeText.setText("Bem vindo ao EzyChat");
  }
}