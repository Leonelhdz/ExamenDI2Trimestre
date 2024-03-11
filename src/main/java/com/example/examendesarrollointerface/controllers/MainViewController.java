package com.example.examendesarrollointerface.controllers;

import com.example.examendesarrollointerface.DAO;
import com.example.examendesarrollointerface.domain.Clientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private Button buttonGuardar;
    @FXML
    private Button buttonDescargar;
    @FXML
    private Label labelResultado;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtEdad;
    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtTalla;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private ChoiceBox<String> txtSexo;
    @FXML
    private ChoiceBox<String> txtTipoAct;
    // HashMap para almacenar los factores de actividad
    private HashMap<String, HashMap<String, Double>> factoresActividad = new HashMap<>();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización de los ChoiceBox
        txtSexo.getItems().addAll("Hombre", "Mujer");
        txtTipoAct.getItems().addAll("Sedentario", "Moderadamente Activo", "Muy Activo");


        buttonGuardar.setOnAction(event -> guardarDatos());

        // Inicialización del HashMap de factores de actividad
        inicializarFactoresActividad();
    }

    private void inicializarFactoresActividad() {
        // Inicialización de los factores de actividad para hombres
        HashMap<String, Double> factoresHombre = new HashMap<>();
        factoresHombre.put("Sedentario", 1.3);
        factoresHombre.put("Moderadamente Activo", 1.6);
        factoresHombre.put("Muy Activo", 2.1);
        factoresActividad.put("Hombre", factoresHombre);

        // Inicialización de los factores de actividad para mujeres
        HashMap<String, Double> factoresMujer = new HashMap<>();
        factoresMujer.put("Sedentario", 1.3);
        factoresMujer.put("Moderadamente Activo", 1.5);
        factoresMujer.put("Muy Activo", 1.9);
        factoresActividad.put("Mujer", factoresMujer);
    }

    private void guardarDatos() {
        // Verificar si todos los campos están completos
        if (camposCompletos()) {
            // Obtener los valores ingresados por el usuario
            String nombre = txtNombre.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            double peso = Double.parseDouble(txtPeso.getText());
            double talla = Double.parseDouble(txtTalla.getText());
            String sexo = txtSexo.getValue();
            String tipoActividad = txtTipoAct.getValue();

            // Calcular el metabolismo basal (GER)
            double ger = calcularGER(sexo, peso, talla, edad);

            // Calcular el gasto energético total (GET)
            double get = ger * factoresActividad.get(sexo).get(tipoActividad);

            // Mostrar los resultados en el Label
            labelResultado.setText(String.format("El cliente %s tiene un GER de %.2f y un GET de %.2f", nombre, ger, get));

        } else {
            labelResultado.setText("Faltan campos por completar.");
        }

    }

    private boolean camposCompletos() {
        return !txtNombre.getText().isEmpty() && !txtEdad.getText().isEmpty() &&
                !txtPeso.getText().isEmpty() && !txtTalla.getText().isEmpty() &&
                txtSexo.getValue() != null && txtTipoAct.getValue() != null;
    }

    private double calcularGER(String sexo, double peso, double talla, int edad) {
        if (sexo.equals("Hombre")) {
            return 66.473 + 13.751 * peso + 5.0033 * talla - 6.755 * edad;
        } else {
            return 655.0955 + 9.463 * peso + 1.8496 * talla - 4.6756 * edad;
        }
    }


    @FXML
    public void generarInforme(ActionEvent actionEvent) throws SQLException, JRException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/informacioncliente", "root", "");
        JasperPrint jasperPrint = JasperFillManager.fillReport("clinica.jasper", null, connection);

        // Mostrar el informe en una ventana
        JasperViewer.viewReport(jasperPrint, false);

        JRPdfExporter exp = new JRPdfExporter();
        exp.setExporterInput(new SimpleExporterInput(jasperPrint));
        exp.setExporterOutput(new SimpleOutputStreamExporterOutput("InformacionClientesCesur.pdf"));
        exp.setConfiguration(new SimplePdfExporterConfiguration());
        exp.exportReport();
    }
}