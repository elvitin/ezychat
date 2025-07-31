package com.vtaveira.ui.components;

import com.vtaveira.ui.util.FXMLLoaderUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OpenedChat extends HBox implements Initializable {

  private final StringProperty usernameProperty = new SimpleStringProperty(this, "username", "<N/A>");
  private final StringProperty spoilerContentProperty = new SimpleStringProperty(this, "spoilerContent", "<N/A>");

  @FXML Text username;
  @FXML Text spoilerContent;

  public OpenedChat() {
    super();
    var loader = FXMLLoaderUtil.createLoader("components/opened-chat.fxml");
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public OpenedChat(String username, String spoilerContent) {
    this();
    this.setUsername(username);
    this.setSpoilerContent(spoilerContent);
  }

  public String getUsername() {
    return this.usernameProperty.get();
  }

  public void setUsername(String username) {
    this.usernameProperty.set(username);
  }

  public String getSpoilerContent() {
    return this.spoilerContentProperty.get();
  }

  public void setSpoilerContent(String spoilerContent) {
    this.spoilerContentProperty.set(spoilerContent);
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.username.textProperty().bind(this.usernameProperty);
    this.spoilerContent.textProperty().bind(this.spoilerContentProperty);
  }
}
