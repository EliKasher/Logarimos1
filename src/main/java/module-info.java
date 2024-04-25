module com.example.logarimos1 {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.instrument;
  requires commons.lang3;


  opens com.example.logarimos1 to javafx.fxml;
  exports com.example.logarimos1;
}