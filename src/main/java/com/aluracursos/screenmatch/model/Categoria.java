package com.aluracursos.screenmatch.model;

public enum Categoria {
    FANTASIA("Fantasy"),
    ACCION("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIMEN("Crimen");


    private String categoriaOmdb;


    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }
    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        if (text.equalsIgnoreCase("Fantasy")){
            return FANTASIA;
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

}
        