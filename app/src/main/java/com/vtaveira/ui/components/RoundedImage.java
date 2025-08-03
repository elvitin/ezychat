package com.vtaveira.ui.components;

import com.vtaveira.ui.util.FXMLLoaderUtil;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
public class RoundedImage extends ImageView implements Initializable {


  private final DoubleProperty sizeProperty = new SimpleDoubleProperty(this, "size", 40.0);
  @FXML private Circle roundedMask;

  public RoundedImage() {
    super();
    this.setup();
  }

  public RoundedImage(Image image) {
    super(image);
    this.setup();
  }

  public RoundedImage(InputStream imageStream) {
    this(new Image(imageStream, 0, 0, true, true));
  }

  public RoundedImage(Image image, double size) {
    super(image);
    this.setSize(size);
    this.setup();
  }

  public RoundedImage(InputStream imageStream, double size) {
    this(new Image(imageStream, 0, 0, true, true), size);
  }

  public double getSize() {
    return this.sizeProperty.get();
  }

  public void setSize(double size) {
    this.sizeProperty.set(size);
  }

  public DoubleProperty sizeProperty() {
    return this.sizeProperty;
  }

  private void setup() {
    var loader = FXMLLoaderUtil.createLoader("components/rounded-image.fxml");
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (java.io.IOException e) {
      throw new IllegalArgumentException(e);
    }
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.configureImage(this.getImage());
    this.imageProperty().addListener((_, _, _) -> this.configureImage(this.getImage()));
    this.sizeProperty().addListener((_, _, _) -> this.configureImage(this.getImage()));
  }

  private void configureImage(Image image) {
    Optional.ofNullable(image).ifPresent(img -> {
      double currentSize = this.getSize();
      double halfSize = currentSize / 2;

      this.setFitWidth(currentSize);
      this.setFitHeight(currentSize);

      this.setPreserveRatio(true);

      this.roundedMask.setRadius(halfSize);
      this.roundedMask.setCenterX(halfSize);
      this.roundedMask.setCenterY(halfSize);

      double imageWidth = img.getWidth();
      double imageHeight = img.getHeight();

      double viewportSize;
      double viewportX = 0;
      double viewportY = 0;

      if (imageWidth > imageHeight) {
        viewportSize = imageHeight;
        viewportX = (imageWidth - viewportSize) / 2;
      } else {
        viewportSize = imageWidth;
        viewportY = (imageHeight - viewportSize) / 2;
      }
      this.setViewport(new Rectangle2D(viewportX, viewportY, viewportSize, viewportSize));
    });
  }
}
