package com.vtaveira.ui.util;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lombok.experimental.UtilityClass;


@UtilityClass
public class AlertUtil {

  public void showInfo(String title, String message) {
    AlertUtil.showAlert(AlertType.INFORMATION, title, message);
  }

  public void showError(String title, String message) {
    AlertUtil.showAlert(AlertType.ERROR, title, message);
  }

  public void showWarning(String title, String message) {
    AlertUtil.showAlert(AlertType.WARNING, title, message);
  }

  private void showAlert(AlertType type, String title, String message) {
    var alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
