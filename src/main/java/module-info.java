module mydrinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens mydrinkshop.ui to javafx.fxml;
    exports mydrinkshop.ui;

    opens mydrinkshop.domain to javafx.base;
    exports mydrinkshop.domain;
}