package com.talent.foro.ventanas;

import com.talent.foro.modelos.Mensaje;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Creador {
    
    public static List<Text> newMensajeTextList(Mensaje mensaje) {
        Text usuario = new Text();
        usuario.setStyle("-fx-fill: #3C64B4; -fx-font-weight: bold;");
        usuario.setText(mensaje.getUsuario() + ' ');
        
        Text hora = new Text();
        hora.setStyle("-fx-fill: gray;");
        hora.setText(mensaje.getFecha().toLocalTime().toString() + '\n');
        
        Text mensajeText = new Text();
        mensajeText.setText(mensaje.getMensaje() + "\n\n");
        
        return Arrays.asList(usuario, hora, mensajeText);
    }
    
    public static Node newUsuarioItem(String nombreUsuario) {
        Label label = new Label(nombreUsuario);
        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        vbox.getStyleClass().add("padding-chico");
        return vbox;
    }
    
}
