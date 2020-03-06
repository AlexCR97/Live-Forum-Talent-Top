package com.talent.foro.validadores;

public class ValidadorContrasena extends Validador<String> {

    @Override
    public boolean validar(String objeto) {
        
        if (objeto == null) {
            return false;
        }
        
        if (objeto.length() < 6) {
            return false;
        }
        
        return true;
    }
    
}
