package com.aluracursos.screenmatch.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    // Realiza una petición HTTP GET a la URL indicada y devuelve el cuerpo como String.
    // Actualmente lanza RuntimeException en caso de IOException o InterruptedException.
    // Mejoras posibles: configurar timeouts, manejar códigos de estado distintos de 200 y
    // devolver Optional<String> o lanzar una excepción específica para poder distinguir causas.
    public String obtenerDatos(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String json = response.body();
        return json;
    }
}
