package com.vtaveira.ui.controller;

import com.vtaveira.client.service.AuthService;
import com.vtaveira.domain.event.EventBus;
import com.vtaveira.domain.event.MessageEvent;
import com.vtaveira.ui.util.AlertUtil;
import com.vtaveira.ui.util.FXMLLoaderUtil;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Slf4j
public class LoginController implements Initializable, CleanableController {

  private final AuthService authService = new AuthService();
  private final EventBus eventBus = EventBus.getInstance();
  private final BooleanProperty processing = new SimpleBooleanProperty(false);
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button loginButton;
  private final Consumer<MessageEvent.LoginResponse> loginResponseHandler = this::handleLoginResponse;
  @FXML
  private Button registerButton;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.setupEventListeners();
    this.setupValidation();
  }

  private void setupEventListeners() {
    this.eventBus.subscribe(MessageEvent.LoginResponse.class, this.loginResponseHandler);
  }

  public void cleanup() {
    this.eventBus.unsubscribe(MessageEvent.LoginResponse.class, this.loginResponseHandler);
  }

  private void setupValidation() {
    BooleanBinding fieldsEmpty = this.usernameField.textProperty().isEmpty()
        .or(this.passwordField.textProperty().isEmpty());
    this.loginButton.disableProperty().bind(fieldsEmpty.or(processing));
  }

  @FXML
  private void handleLogin() {
    var username = usernameField.getText().trim();
    var password = passwordField.getText();

    if (username.isEmpty() || password.isEmpty()) {
      AlertUtil.showError("Erro", "Por favor, preencha todos os campos.");
      return;
    }

    this.processing.set(true);
    this.authService.login(username, password)
        .whenComplete((_, throwable) ->
            Platform.runLater(() -> {
              this.processing.set(false);
              if (throwable != null) {
                AlertUtil.showError("Erro de Conexão", "Não foi possível conectar ao servidor.");
              }
            })
        );
  }

  @FXML
  private void handleRegister() {
    try {
      var stage = (Stage) registerButton.getScene().getWindow();
      FXMLLoaderUtil.changeScene(stage, "register.fxml");
    } catch (Exception _) {
      AlertUtil.showError("Erro", "Não foi possível abrir a tela de registro.");
    }
  }


  private void handleLoginResponse(MessageEvent.LoginResponse event) {
    Platform.runLater(() -> {
      var response = event.response();
      if (response.getSuccess()) {
        openMainChat();
      } else {
        AlertUtil.showError("Erro de Login", response.getErrorMessage());
      }
    });
  }

  private void openMainChat() {
    try {
      var stage = (Stage) loginButton.getScene().getWindow();
      FXMLLoaderUtil.changeScene(stage, "main-chat.fxml");
      stage.setMaximized(true);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      AlertUtil.showError("Erro", "Não foi possível abrir o chat principal.");
    }
  }
}
