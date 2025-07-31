package com.vtaveira.ui.components;

import com.vtaveira.ui.util.FXMLLoaderUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatMessage extends HBox implements Initializable {

  private final BooleanProperty isFromMeProperty = new SimpleBooleanProperty(this, "isFromMe", true);
  private final StringProperty contentProperty = new SimpleStringProperty(this, "content");


  @FXML Text content;
  @FXML TextFlow messageCard;
  private boolean isFrozen = false;

  public ChatMessage() {
    super();
    var loader = FXMLLoaderUtil.createLoader("components/chat-message.fxml");
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (java.io.IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public ChatMessage(String content) {
    this();
    this.setContent(content);
  }

  public ChatMessage(String content, boolean isFromMe) {
    this();
    this.setContent(content);
    this.setIsFromMe(isFromMe);
  }

  public boolean getIsFromMe() {
    return this.isFromMeProperty.get();
  }

  public void setIsFromMe(boolean isFromMe) {
    if (isFrozen) {
      throw new IllegalStateException("Cannot change isFromMe after the message is frozen.");
    }

    this.isFromMeProperty.set(isFromMe);
    this.isFrozen = true;

    updateStyle();
  }

  public String getContent() {
    return this.contentProperty.get();
  }

  public void setContent(String text) {
    this.contentProperty.set(text);
  }

  public StringProperty contentProperty() {
    return this.contentProperty;
  }
  
  private void updateStyle() {
    getStyleClass().removeAll(
        "chat_message__left_container",
        "chat_message__right_container"
    );
    getStyleClass().add(this.isFromMeProperty.get() ? "chat_message__right_container" : "chat_message__left_container");

    messageCard.getStyleClass().removeAll(
        "chat_message__sent_message",
        "chat_message__received_message"
    );
    messageCard.getStyleClass().add(this.isFromMeProperty.get() ? "chat_message__sent_message" : "chat_message__received_message");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.content.textProperty().bind(this.contentProperty());
    updateStyle();
  }
}