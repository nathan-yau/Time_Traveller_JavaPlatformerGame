module ca.bcit.comp2522.termproject.pix {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens ca.bcit.comp2522.termproject.pix to javafx.fxml;
    exports ca.bcit.comp2522.termproject.pix;
    exports ca.bcit.comp2522.termproject.pix.screens;
    exports ca.bcit.comp2522.termproject.pix.model;
    exports ca.bcit.comp2522.termproject.pix.model.player;
    exports ca.bcit.comp2522.termproject.pix.model.weapon;
    exports ca.bcit.comp2522.termproject.pix.model.AttackEffect;
    exports ca.bcit.comp2522.termproject.pix.model.block;
    exports ca.bcit.comp2522.termproject.pix.model.pickupitem;
    exports ca.bcit.comp2522.termproject.pix.model.Enemy;
}