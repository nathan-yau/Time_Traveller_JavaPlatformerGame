module ca.bcit.comp2522.termproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.bcit.comp2522.termproject to javafx.fxml;
    exports ca.bcit.comp2522.termproject;
}