module qkm1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens qkm1 to javafx.fxml;
    exports qkm1;
}
