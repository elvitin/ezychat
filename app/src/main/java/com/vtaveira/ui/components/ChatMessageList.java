package com.vtaveira.ui.components;

import com.vtaveira.ui.util.FXMLLoaderUtil;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
public class ChatMessageList extends VBox implements Initializable {

  private final Map<Long, ChatMessage> messagesById = new HashMap<>();

  public ChatMessageList() {
    super();
    var loader = FXMLLoaderUtil.createLoader("components/chat-message-list.fxml");
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void addMessage(long id, String content, boolean fromMe) {
    this.messagesById.computeIfAbsent(id, _ -> {
      this.getChildren().add(new ChatMessage(content, fromMe));
      return new ChatMessage(content, fromMe);
    });
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.info("chatMessageList initialized");
  }
}
