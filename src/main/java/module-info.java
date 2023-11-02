module ca.bcit.comp2522.termproject.pix {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.bcit.comp2522.termproject.pix to javafx.fxml;
    exports ca.bcit.comp2522.termproject.pix;
}