package com.vtaveira.ui;

import com.vtaveira.infra.config.AppConfig;
import com.vtaveira.infra.database.DatabaseConfig;
import com.vtaveira.ui.util.FXMLLoaderUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class EzyChatApp extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    AppConfig.initialize();
    DatabaseConfig.initialize();

    var loginScene = FXMLLoaderUtil.loadScene("login.fxml");
    primaryStage.setTitle("EzyChat App");
    primaryStage.setScene(loginScene);
    primaryStage.setMinWidth(500);
    primaryStage.setMinHeight(400);
    primaryStage.show();
    log.info("EzyChat Client Application started");
  }

  @Override
  public void stop() throws Exception {
    AppConfig.shutdown();
    super.stop();
  }
}