package com.talent.foro.ventanas.inicio;

import com.talent.foro.modelos.Mensaje;
import com.talent.foro.modelos.Usuario;
import com.talent.foro.servicios.ServicioCache;
import com.talent.foro.servicios.ServicioConsultas;
import com.talent.foro.ventanas.Creador;
import com.talent.foro.ventanas.VentanaUtils;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;

public class InicioController implements Initializable {
    
    @FXML
    private Label lbUsuario;
    
    @FXML
    private Button btCerrarSesion;
    
    @FXML
    private ScrollPane scrollMensajes;
    
    @FXML
    private StackPane spMensajes;
    
    @FXML
    private TextField tfMensaje;
    
    @FXML
    private Button btEnviar;
    
    private final ServicioCache servicioCache = ServicioCache.getInstance();
    private final ServicioConsultas servicioConsultas = new ServicioConsultas();
    private final TextFlow textFlowMensajes = new TextFlow();
    private final Usuario usuario = (Usuario) servicioCache.getCache("usuario");
    private final VentanaUtils ventana = new VentanaUtils();
    
    private List<Mensaje> mensajesAnteriores = new ArrayList<>();
    private ScheduledFuture hiloDeMensajes;
    private Event ultimoEvento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbUsuario.setText(usuario.getUsuario());
        spMensajes.getChildren().add(textFlowMensajes);
        
        btCerrarSesion.setOnMouseClicked(e -> {
            ultimoEvento = e;
            
            Platform.runLater(() -> {
                detenerHiloDeMensajes();
                ventana.abrirVentana("/com/talent/foro/ventanas/login/Login.fxml", ultimoEvento.getSource()).show();
            });
        });
        
        scrollMensajes.vvalueProperty().bind(spMensajes.heightProperty());
        
        tfMensaje.setOnKeyPressed(e -> {
            ultimoEvento = e;
            
            if (e.getCode().equals(KeyCode.ENTER)) {
                enviarMensaje();
            }
        });
        
        btEnviar.setOnMouseClicked(e -> {
            ultimoEvento = e;
            
            enviarMensaje();
        });
        
        ejecutarHiloDeMensajes();
    }
    
    private void enviarMensaje() {
        String mensajeTexto = tfMensaje.getText();
            
        if (mensajeTexto.isEmpty()) {
            return;
        }

        btEnviar.setDisable(true);

        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje(mensajeTexto);
        mensaje.setUsuario(usuario.getUsuario());
        mensaje.setFecha(LocalDateTime.now());

        servicioConsultas.addMensaje(mensaje)
        .thenAccept(mensajeEnviado -> {
            if (mensajeEnviado) {
                Platform.runLater(() -> {
                    btEnviar.setDisable(false);
                    tfMensaje.clear();

                    // TODO actualizar foro
                });
            }
            else {
                Platform.runLater(() -> {
                    btEnviar.setDisable(false);
                    tfMensaje.clear();
                    
                    ventana.mostrarAlertaMensaje("No se pudo enviar el mensaje :(");
                });
            }
        })
        .exceptionally(ex -> {
            ex.printStackTrace();

            Platform.runLater(() -> {
                btEnviar.setDisable(false);
                ventana.mostrarAlertaMensaje(ex.getMessage());
            });

            return null;
        });
    }
    
    private void ejecutarHiloDeMensajes() {
        hiloDeMensajes = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            servicioConsultas.getMensajes()
            .thenAccept(mensajesNuevos -> {
                List<Mensaje> mensajesNuevosCopia = new ArrayList<>();
                mensajesNuevosCopia.addAll(mensajesNuevos);
                
                mensajesNuevos.removeAll(mensajesAnteriores);
                mensajesNuevos.forEach(mensajeItem -> {
                    Platform.runLater(() -> {
                        Creador.newMensajeTextList(mensajeItem).forEach(textFlowMensajes.getChildren()::add);
                    });
                });
                
                mensajesAnteriores.clear();
                mensajesAnteriores.addAll(mensajesNuevosCopia);
            })
            .exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
        }, 0, 2, TimeUnit.SECONDS);
    }
    
    private void detenerHiloDeMensajes() {
        if (hiloDeMensajes != null) {
            hiloDeMensajes.cancel(true);
        }
    }
}
