package com.aluracursos.screenmatch.model;

import java.util.Locale;

/**
 * Enum que representa categorías/géneros de series. Provee utilidades para
 * convertir cadenas (posiblemente provenientes del campo Genre de OMDB)
 * en valores del enum.
 */
public enum Categoria {
    FANTASIA("Fantasy", "Fantasia"),
    ACCION("Action","Acción"),
    ROMANCE("Romance","Romance"),
    COMEDIA("Comedy","Comedia"),
    DRAMA("Drama","Drama"),
    CRIMEN("Crime","Crimen");


    private String categoriaOmdb;
    private String categoriaEspanol;


    Categoria(String categoriaOmdb, String categoriaEspanol){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEspanol = categoriaEspanol;
    }
    // Convierte una cadena (posiblemente del campo Genre de OMDB) a la constante
    // del enum. Si no encuentra una coincidencia devuelve null en lugar de lanzar.
    // Acepta múltiples géneros separados por "," y múltiples variantes en inglés.
    public static Categoria fromString(String text) {
        if (text == null) return null;
        // Si la cadena contiene varios géneros, iteramos por cada token
        String[] tokens = text.split(",");
        for (String raw : tokens) {
            String t = raw.trim();
            // Normalizamos a minúsculas para comparaciones simples
            String lower = t.toLowerCase(Locale.ROOT);
            for (Categoria categoria : Categoria.values()) {
                if (categoria.categoriaOmdb.equalsIgnoreCase(t) || categoria.name().equalsIgnoreCase(t)) {
                    return categoria;
                }
            }
            // Comparaciones por palabra clave más permisivas
            if (lower.contains("fantasy") || lower.contains("fantasia")) return FANTASIA;
            if (lower.contains("action") || lower.contains("accion")) return ACCION;
            if (lower.contains("romance")) return ROMANCE;
            if (lower.contains("comedy") || lower.contains("comedia")) return COMEDIA;
            if (lower.contains("drama")) return DRAMA;
            if (lower.contains("crime") || lower.contains("crimen")) return CRIMEN;
        }
        // No hay coincidencias claras -> devolvemos null para que el llamador
        // pueda decidir qué hacer (p. ej. asignar una categoría por defecto).
        return null;
    }
    public static Categoria fromEspanol(String text) {
        if (text == null) return null;
        // Si la cadena contiene varios géneros, iteramos por cada token
        String[] tokens = text.split(",");
        for (String raw : tokens) {
            String t = raw.trim();
            // Normalizamos a minúsculas para comparaciones simples
            String lower = t.toLowerCase(Locale.ROOT);
            for (Categoria categoria : Categoria.values()) {
                if (categoria.categoriaEspanol.equalsIgnoreCase(t) || categoria.name().equalsIgnoreCase(t)) {
                    return categoria;
                }
            }
            // Comparaciones por palabra clave más permisivas
            if (lower.contains("fantasy") || lower.contains("fantasia")) return FANTASIA;
            if (lower.contains("action") || lower.contains("accion")) return ACCION;
            if (lower.contains("romance")) return ROMANCE;
            if (lower.contains("comedy") || lower.contains("comedia")) return COMEDIA;
            if (lower.contains("drama")) return DRAMA;
            if (lower.contains("crime") || lower.contains("crimen")) return CRIMEN;
        }
        // No hay coincidencias claras -> devolvemos null para que el llamador
        // pueda decidir qué hacer (p. ej. asignar una categoría por defecto).
        return null;
    }

}
