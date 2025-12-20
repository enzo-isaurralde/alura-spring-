package com.aluracursos.screenmatch.service;

public interface IConvierteDatos {
    // Contrato: dado un JSON y una clase, devolver una instancia de la clase mapeada.
    <T> T obtenerDatos(String json, Class<T> clase);
}
