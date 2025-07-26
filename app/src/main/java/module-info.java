module com.vtaveira.ui {
  requires com.vtaveira.common;
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  requires java.logging;
  requires org.slf4j;
  requires static lombok;

  opens com.vtaveira.ui.controller to javafx.fxml;
  exports com.vtaveira;
}