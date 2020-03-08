package com.talent.foro.ventanas.registro;

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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class RegistroController implements Initializable {

    @FXML
    private TextField tfUsuario;
    
    @FXML
    private PasswordField pfContrasena;
    
    @FXML
    private PasswordField pfConfirmarContrasena;
    
    @FXML
    private Button btRegistrarse;
    
    @FXML
    private Label lbIniciaSesion;
    
    private final ServicioCache servicioCache = ServicioCache.getInstance();
    private final ServicioConsultas servicioConsultas = new ServicioConsultas();
    private final VentanaUtils ventana = new VentanaUtils();
    private Event ultimoEvento;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfUsuario.setOnKeyPressed(e -> {
            ultimoEvento = e;
            
            if (e.getCode().equals(KeyCode.ENTER)) {
                intentarRegistro();
            }
        });
        
        pfContrasena.setOnKeyPressed(e -> {
            ultimoEvento = e;
            
            if (e.getCode().equals(KeyCode.ENTER)) {
                intentarRegistro();
            }
        });
        
        pfConfirmarContrasena.setOnKeyPressed(e -> {
            ultimoEvento = e;
            
            if (e.getCode().equals(KeyCode.ENTER)) {
                intentarRegistro();
            }
        });
        
        lbIniciaSesion.setOnMouseClicked(e -> {
            ultimoEvento = e;
            Stage ventanaLogin = ventana.abrirVentana("/com/talent/foro/ventanas/login/Login.fxml", ultimoEvento.getSource());
            ventanaLogin.show();
        });
        
        btRegistrarse.setOnMouseClicked(e -> {
            ultimoEvento = e;
            
            intentarRegistro();
        });
    }
    
    private void iniciarSesion(Usuario usuario) {
        servicioCache.agregarCache("usuario", usuario);
        
        Stage ventanaInicio = ventana.abrirVentana("/com/talent/foro/ventanas/inicio/Inicio.fxml", ultimoEvento.getSource());
        ventanaInicio.show();
    }
    
    private void intentarRegistro() {
        String usuario = tfUsuario.getText();
        String contrasena = pfContrasena.getText();
        String confirmarContrasena = pfConfirmarContrasena.getText();

        if (!validarDatos(usuario, contrasena, confirmarContrasena)) {
            return;
        }

        btRegistrarse.setDisable(true);

        servicioConsultas.verificarExistenciaUsuario(usuario)
        .thenAccept(usuarioExiste -> {
            if (usuarioExiste) {
                Platform.runLater(() -> {
                    btRegistrarse.setDisable(false);
                    ventana.mostrarAlertaMensaje("El nombre de usuario '" + usuario + "' no esta disponible :(");
                });
            }
            else {
                registrarUsuario(usuario, contrasena);
            }
        })
        .exceptionally(ex -> {
            ex.printStackTrace();

            Platform.runLater(() -> {
                btRegistrarse.setDisable(false);
                ventana.mostrarAlertaMensaje(ex.getMessage());
            });

            return null;
        });
    }
    
    private void registrarUsuario(String nombreUsuario, String contrasena) {
        Usuario usuario = new Usuario();
        usuario.setUsuario(nombreUsuario);
        usuario.setContrasena(contrasena);
        
        servicioConsultas.addUsuario(usuario)
        .thenAccept(usuarioAgregado -> {
            if (usuarioAgregado) {
                Platform.runLater(() -> {
                    iniciarSesion(usuario);
                });
            }
            else {
                Platform.runLater(() -> {
                    btRegistrarse.setDisable(false);
                    ventana.mostrarAlertaMensaje("No se pudo registrar al usuario :(");
                });
            }
        })
        .exceptionally(ex -> {
            ex.printStackTrace();
            
            Platform.runLater(() -> {
                btRegistrarse.setDisable(false);
                ventana.mostrarAlertaMensaje(ex.getMessage());
            });
            
            return null;
        });
    }
    
    private boolean validarDatos(String usuario, String contrasena, String confirmarContrasena) {
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
        
        if (!contrasena.equals(confirmarContrasena)) {
            ventana.mostrarAlertaMensaje("Las contraseñas no coinciden");
            return false;
        }
        
        return true;
    }
}
