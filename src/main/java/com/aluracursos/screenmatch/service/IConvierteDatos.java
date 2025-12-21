package com.aluracursos.screenmatch.service;

public interface IConvierteDatos {
    // Contrato: dado un JSON y una clase, devolver una instancia de la clase mapeada.
    // Implementaciones deben lanzar RuntimeException (o específica) si el JSON no es válido.
    <T> T obtenerDatos(String json, Class<T> clase);
}
