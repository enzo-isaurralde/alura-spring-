package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.model.DatosSerie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementación simple del conversor de JSON a objetos Java usando Jackson.
 * Convierte una cadena JSON a una instancia de la clase indicada.
 *
 * Nota: en caso de JSON inválido lanzamos RuntimeException envuelta; en entornos
 * reales podría preferirse una excepción específica o un Optional para manejar fallos.
 */
public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            // Usa Jackson ObjectMapper para convertir el JSON en la clase indicada.
            // Si el JSON no coincide con la estructura esperada, Jackson lanzará
            // JsonProcessingException y aquí lo transformamos en RuntimeException.
            return objectMapper.readValue(json,clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
