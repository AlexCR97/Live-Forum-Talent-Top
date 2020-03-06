package com.talent.foro.servicios;

import java.util.HashMap;
import java.util.Map;

public class ServicioCache {
    
    private static ServicioCache SERVICIO_CACHE;
    private final Map<String, Object> cache = new HashMap<>();
    
    private ServicioCache() { }
    
    public void agregarCache(String id, Object datos) {
        cache.put(id, datos);
    }
    
    public Object getCache(String id) {
        return cache.put(id, cache);
    }
    
    public static ServicioCache getInstance() {
        if (SERVICIO_CACHE == null) {
            SERVICIO_CACHE = new ServicioCache();
        }
        
        return SERVICIO_CACHE;
    }
}
