package com.talent.foro.validadores;

public class ValidadorNombreUsuario extends Validador<String> {

    @Override
    public boolean validar(String objeto) {
        if (objeto == null) {
            return false;
        }
        
        if (objeto.isEmpty()) {
            return false;
        }
        
        if (objeto.length() > 10) {
            return false;
        }
        
        return true;
    }
    
}
