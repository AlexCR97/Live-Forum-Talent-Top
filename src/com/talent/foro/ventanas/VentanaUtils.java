package com.talent.foro.ventanas;

import com.talent.foro.ventanas.login.LoginController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class VentanaUtils {
    
    public void abrirVentana(String ruta) {
        abrirVentana(ruta, null);
    }
    
    public void abrirVentana(String ruta, Object eventSource) {
        try {
            Parent newRoot = FXMLLoader.load(getClass().getResource(ruta));
            Scene newScene = new Scene(newRoot);
            Stage newStage = new Stage();
            
            newStage.setScene(newScene);
            newStage.show();
            
            if (eventSource != null) {
                Node source = (Node) eventSource;
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        }
        catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Alert mostrarAlertaCargando() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Alert alert = new Alert(AlertType.NONE);
        alert.setGraphic(progressIndicator);
        alert.show();
        return alert;
    }
    
    public Alert mostrarAlertaMensaje(String mensaje) {
        Alert alert = new Alert(AlertType.CONFIRMATION, mensaje);
        alert.showAndWait();
        return alert;
    }
}
