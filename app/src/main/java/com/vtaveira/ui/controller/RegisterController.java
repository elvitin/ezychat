package com.vtaveira.ui.controller;

import com.vtaveira.client.service.AuthService;
import com.vtaveira.domain.event.EventBus;
import com.vtaveira.domain.event.MessageEvent;
import com.vtaveira.ui.util.AlertUtil;
import com.vtaveira.ui.util.FXMLLoaderUtil;
import com.vtaveira.util.ValidationUtil;
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
public class RegisterController implements Initializable, CleanableController {
  private final AuthService authService = new AuthService();
  private final EventBus eventBus = EventBus.getInstance();
  private final BooleanProperty processing = new SimpleBooleanProperty(false);
  @FXML
  private TextField usernameField;
  @FXML
  private TextField fullNameField;
  @FXML
  private TextField emailField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private PasswordField confirmPasswordField;
  @FXML
  private Button registerButton;
  @FXML
  private Button backButton;
  private final Consumer<MessageEvent.Ack> registerResponseHandler = this::handleRegisterResponse;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.setupEventListeners();
    this.setupValidation();
  }

  private void setupEventListeners() {
    eventBus.subscribe(MessageEvent.Ack.class, this.registerResponseHandler);
  }

  private void setupValidation() {
    BooleanBinding fieldsEmpty = this.usernameField.textProperty().isEmpty()
        .or(this.fullNameField.textProperty().isEmpty())
        .or(this.emailField.textProperty().isEmpty())
        .or(this.passwordField.textProperty().isEmpty())
        .or(this.confirmPasswordField.textProperty().isEmpty());

    this.registerButton.disableProperty().bind(fieldsEmpty.or(processing));
  }

  @FXML
  private void handleRegister() {
    var username = usernameField.getText().trim();
    var fullName = fullNameField.getText().trim();
    var email = emailField.getText().trim();
    var password = passwordField.getText();
    var confirmPassword = confirmPasswordField.getText();

    // Validações
    if (!ValidationUtil.isValidEmail(email)) {
      AlertUtil.showError("Erro", "Email inválido.");
      return;
    }

    if (!password.equals(confirmPassword)) {
      AlertUtil.showWarning("Erro", "As senhas não coincidem.");
      return;
    }

    if (password.length() < 6) {
      AlertUtil.showError("Erro", "A senha deve ter pelo menos 6 caracteres.");
      return;
    }

    log.debug("Disabling register button...");
    this.processing.set(true);
    this.authService.register(username, fullName, email, password)
        .whenComplete((success, throwable) -> {
          log.info("Register response: {}", success);
          Platform.runLater(() -> {
            this.processing.set(false);
            if (throwable != null) {
              AlertUtil.showError("Erro de Conexão",
                  "Não foi possível conectar ao servidor.");
            }
          });
        });
  }

  @FXML
  private void handleBack() {
    try {
      var stage = (Stage) backButton.getScene().getWindow();
      FXMLLoaderUtil.changeScene(stage, "login.fxml");
    } catch (Exception _) {
      AlertUtil.showError("Erro", "Não foi possível voltar para a tela de login.");
    }
  }

  private void handleRegisterResponse(MessageEvent.Ack event) {
    Platform.runLater(() -> {
      var ack = event.ack();
      if (ack.getSucess()) {
        AlertUtil.showInfo("Sucesso", "Usuário registrado com sucesso!");
        handleBack();
      } else {
        AlertUtil.showError("Erro", ack.getMessage());
      }
    });
  }

  /**
   *
   */
  @Override
  public void cleanup() {
    this.eventBus.unsubscribe(MessageEvent.Ack.class, this.registerResponseHandler);
  }
}
