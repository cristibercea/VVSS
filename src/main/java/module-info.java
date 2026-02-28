module drinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;

    opens mydrinkshop.ui to javafx.fxml;
    exports mydrinkshop.ui;

    opens mydrinkshop.domain to javafx.base;
    exports mydrinkshop.domain;
}