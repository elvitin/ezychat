package com.vtaveira.util;

import com.vtaveira.EzyChatApp;
import javafx.scene.image.Image;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
@UtilityClass
public class ResourcesLoader {
  public InputStream loadFromAssets(String filename) {
    return EzyChatApp.class.getResourceAsStream("assets/" + filename);
  }

  public Image loadImageFromAssets(String filename) {
    return new Image(ResourcesLoader.loadFromAssets(filename));
  }
}
