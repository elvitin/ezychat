package com.vtaveira.ui.components;

import com.vtaveira.ui.util.FXMLLoaderUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class FriendProfile extends HBox implements Initializable {

  private final StringProperty usernameProperty = new SimpleStringProperty(this, "username", "<N/A>");
  private final StringProperty statusContentProperty = new SimpleStringProperty(this, "statusContent", "<N/A>");

  @FXML Text username;
  @FXML Text statusContent;

  public FriendProfile() {
    super();
    var loader = FXMLLoaderUtil.createLoader("components/friend-profile.fxml");
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (java.io.IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public FriendProfile(String username) {
    this();
    this.setUsername(username);
  }

  public FriendProfile(String username, String statusContent) {
    this(username);
    this.setStatusContent(statusContent);
  }

  public FriendProfile(String username, String statusContent, Runnable onClickAction) {
    this(username, statusContent);
    this.setOnMouseClicked(_ -> onClickAction.run());
  }

  public FriendProfile(String username, Runnable onClickAction) {
    this(username);
    this.setOnMouseClicked(_ -> onClickAction.run());
  }


  public String getUsername() {
    return this.usernameProperty.get();
  }

  public void setUsername(String username) {
    this.usernameProperty.set(username);
  }

  public void setStatusContent(String statusContent) {
    this.statusContentProperty.set(statusContent);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.username.textProperty().bind(this.usernameProperty);
    this.statusContent.textProperty().bind(this.statusContentProperty);
  }
}
