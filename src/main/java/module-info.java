module ca.bcit.comp2522.termproject.pix {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.bcit.comp2522.termproject.pix to javafx.fxml;
    exports ca.bcit.comp2522.termproject.pix;
    exports ca.bcit.comp2522.termproject.pix.screens;
    exports ca.bcit.comp2522.termproject.pix.model.player;
    exports ca.bcit.comp2522.termproject.pix.model.weapon;
    exports ca.bcit.comp2522.termproject.pix.model.AttackEffect;
}