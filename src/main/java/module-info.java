module com.example.examendesarrollointerface {
    requires javafx.controls;
    requires javafx.fxml;

    // Añadido
    requires lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires jasperreports;


    opens com.example.examendesarrollointerface to javafx.fxml;
    exports com.example.examendesarrollointerface;
    exports com.example.examendesarrollointerface.controllers;
    opens com.example.examendesarrollointerface.controllers;
    opens com.example.examendesarrollointerface.domain;
}