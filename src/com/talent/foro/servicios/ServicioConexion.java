package com.talent.foro.servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ServicioConexion {
    
    private final String servidor;
    private final String puerto;
    private final String baseDeDatos;
    private final String usuario;
    private final String contrasena;
    
    private Connection conexion;

    public ServicioConexion() {
        servidor = "remotemysql.com";
        puerto = "3306";
        baseDeDatos = "WERiQ3oK0o";
        usuario = "WERiQ3oK0o";
        contrasena = "dUSrrZAeyM";
    }
    
    public ServicioConexion(String servidor, String puerto, String baseDeDatos, String usuario, String contrasena) {
        this.servidor = servidor;
        this.puerto = puerto;
        this.baseDeDatos = baseDeDatos;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
    
    public boolean cerrarConexion() {
        if (conexion == null) {
            return true;
        }
        
        try {
            conexion.close();
            return true;
        }
        catch (SQLException ex) {
            return false;
        }
    }
    
    public CompletableFuture<Connection> getConexion() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = getUrlConexion();
                conexion = DriverManager.getConnection(url, usuario, contrasena);
                
                return conexion;
            }
            catch (SQLException ex) {
                throw new CompletionException(ex);
            }
        });
    }
    
    public String getUrlConexion() {
        return String.format(
                "jdbc:mysql://%s:%s/%s?"
                        + "useUnicode=true&"
                        + "useJDBCCompliantTimezoneShift=true&"
                        + "useLegacyDatetimeCode=false&"
                        + "serverTimezone=UTC",
                servidor,
                puerto,
                baseDeDatos
        );
    }
    
}
