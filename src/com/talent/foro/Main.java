package com.talent.foro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/talent/foro/ventanas/login/Login.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/com/talent/foro/ventanas/registro/Registro.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/com/talent/foro/ventanas/inicio/Inicio.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
