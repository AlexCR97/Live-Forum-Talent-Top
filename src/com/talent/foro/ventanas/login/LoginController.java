package com.talent.foro.ventanas.login;

import com.talent.foro.modelos.Usuario;
import com.talent.foro.servicios.ServicioCache;
import com.talent.foro.servicios.ServicioConsultas;
import com.talent.foro.validadores.ValidadorContrasena;
import com.talent.foro.validadores.ValidadorNombreUsuario;
import com.talent.foro.ventanas.VentanaUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    
    @FXML
    private TextField tfUsuario;
    
    @FXML
    private PasswordField pfContrasena;
    
    @FXML
    private Button btIniciar;
    
    @FXML
    private Label lbRegistrate;
    
    private Event ultimoEvento;
    
    private final ServicioCache servicioCache = ServicioCache.getInstance();
    private final ServicioConsultas servicioConsultas = new ServicioConsultas();
    private final VentanaUtils ventana = new VentanaUtils();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btIniciar.setOnMouseClicked(e -> {
            ultimoEvento = e;
            
            String nombreUsuario = tfUsuario.getText();
            String contrasena = pfContrasena.getText();
            
            if (!validarDatos(nombreUsuario, contrasena)) {
                return;
            }
            
            btIniciar.setDisable(true);
            
            servicioConsultas.getUsuario(nombreUsuario, contrasena)
            .thenAccept(usuario -> {
                Platform.runLater(() -> {
                    btIniciar.setDisable(false);
                    iniciarSesion(usuario);
                });
            })
            .exceptionally(ex -> {
                ex.printStackTrace();
                
                Platform.runLater(() -> {
                    btIniciar.setDisable(false);
                    ventana.mostrarAlertaMensaje(ex.getMessage());
                });
                
                return null;
            });
        });
        
        lbRegistrate.setOnMouseClicked(e -> {
            ultimoEvento = e;
            ventana.abrirVentana("/com/talent/foro/ventanas/registro/Registro.fxml", ultimoEvento.getSource());
        });
    }
    
    private void iniciarSesion(Usuario usuario) {
        servicioCache.agregarCache("usuario", usuario);
        ventana.abrirVentana("/com/talent/foro/ventanas/inicio/Inicio.fxml", ultimoEvento.getSource());
    }
    
    private boolean validarDatos(String usuario, String contrasena) {
        ValidadorNombreUsuario validadorNombreUsuario = new ValidadorNombreUsuario();
        
        if (!validadorNombreUsuario.validar(usuario)) {
            ventana.mostrarAlertaMensaje("El nombre de usuario no es valido");
            return false;
        }
        
        ValidadorContrasena validadorContrasena = new ValidadorContrasena();
        
        if (!validadorContrasena.validar(contrasena)) {
            ventana.mostrarAlertaMensaje("La contraseña no es valida");
            return false;
        }
        
        return true;
    }
}
