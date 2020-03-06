package com.talent.foro.ventanas.inicio;

import com.talent.foro.modelos.Usuario;
import com.talent.foro.servicios.ServicioCache;
import com.talent.foro.servicios.ServicioConsultas;
import com.talent.foro.ventanas.VentanaUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;

public class InicioController implements Initializable {
    
    @FXML
    private Menu mnNombreUsuario;
    
    @FXML
    private Menu mnSalir;
    
    private final ServicioCache servicioCache = ServicioCache.getInstance();
    private final ServicioConsultas servicioConsultas = new ServicioConsultas();
    private final Usuario usuario = (Usuario) servicioCache.getCache("usuario");
    private final VentanaUtils ventana = new VentanaUtils();
    private Event ultimoEvento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mnNombreUsuario.setText(usuario.getUsuario());
    }
    
}
