package com.talent.foro.servicios;

import com.talent.foro.modelos.Mensaje;
import com.talent.foro.modelos.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class ServicioConsultas {
    
    private final ServicioConexion servicioConexion = new ServicioConexion();
    
    public CompletableFuture<Boolean> addUsuario(Usuario usuario) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conexion = servicioConexion.getConexion().get();
                PreparedStatement statement = conexion.prepareStatement("INSERT INTO usuarios VALUES (?, ?)")) {
                
                statement.setString(1, usuario.getUsuario());
                statement.setString(2, usuario.getContrasena());
                
                return statement.executeUpdate() > 0;
            }
            catch (InterruptedException | ExecutionException | SQLException ex) {
                throw new CompletionException(ex);
            }
        });
    }
    
    public CompletableFuture<Boolean> addMensaje(Mensaje mensaje) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conexion = servicioConexion.getConexion().get();
                PreparedStatement statement = conexion.prepareStatement("INSERT INTO mensajes(mensaje, fecha, usuario) VALUES (?, ?, ?)")) {
                
                statement.setString(1, mensaje.getMensaje());
                statement.setString(2, mensaje.getFecha().toString());
                statement.setString(3, mensaje.getUsuario());
                
                return statement.executeUpdate() > 0;
            }
            catch (InterruptedException | ExecutionException | SQLException ex) {
                throw new CompletionException(ex);
            }
        });
    }
    
    public CompletableFuture<List<Mensaje>> getMensajes() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conexion = servicioConexion.getConexion().get();
                PreparedStatement statement = conexion.prepareStatement("SELECT * FROM mensajes ORDER BY fecha ASC");
                ResultSet resultSet = statement.executeQuery()) {

                List<Mensaje> mensajes = new ArrayList<>();
                
                while (resultSet.next()) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMensaje(resultSet.getString(2));
                    mensaje.setFecha(resultSet.getTimestamp(3).toLocalDateTime());
                    mensaje.setUsuario(resultSet.getString(4));
                    
                    mensajes.add(mensaje);
                }
                
                return mensajes;
            }
            catch (InterruptedException | ExecutionException | SQLException ex) {
                throw new CompletionException(ex);
            }
        });
    }
    
    public CompletableFuture<Usuario> getUsuario(String nombreUsuario, String contrasena) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conexion = servicioConexion.getConexion().get();
                PreparedStatement statement = conexion.prepareStatement("SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?")) {

                statement.setString(1, nombreUsuario);
                statement.setString(2, contrasena);
                ResultSet resultSet = statement.executeQuery();
                
                if (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setUsuario(resultSet.getString(1));
                    usuario.setContrasena(resultSet.getString(2));
                    
                    return usuario;
                }
                
                throw new CompletionException(new Exception("El usuario no existe"));
            }
            catch (InterruptedException | ExecutionException | SQLException ex) {
                throw new CompletionException(ex);
            }
        });
    }

    public CompletableFuture<List<Usuario>> getUsuarios() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conexion = servicioConexion.getConexion().get();
                PreparedStatement statement = conexion.prepareStatement("SELECT * FROM usuarios");
                ResultSet resultSet = statement.executeQuery()) {

                List<Usuario> usuarios = new ArrayList<>();
                
                while (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setUsuario(resultSet.getString(1));
                    usuario.setContrasena(resultSet.getString(2));
                    
                    usuarios.add(usuario);
                }
                
                return usuarios;
            }
            catch (InterruptedException | ExecutionException | SQLException ex) {
                throw new CompletionException(ex);
            }
        });
    }
    
    public CompletableFuture<Boolean> verificarExistenciaUsuario(String usuario) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conexion = servicioConexion.getConexion().get();
                PreparedStatement statement = conexion.prepareStatement("SELECT * FROM usuarios WHERE usuario = ?")) {
                
                statement.setString(1, usuario);
                ResultSet resultSet = statement.executeQuery();
                
                return resultSet.next();
            }
            catch (InterruptedException | ExecutionException | SQLException ex) {
                throw new CompletionException(ex);
            }
        });
    }
}
