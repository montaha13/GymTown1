module com.example.gestionutilisateur {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.pdfbox;
    requires java.prefs;
    requires jbcrypt;
    requires javafx.web;

    opens com.example.gestionutilisateur.controllers to javafx.graphics, javafx.fxml;
    exports com.example.gestionutilisateur;
}