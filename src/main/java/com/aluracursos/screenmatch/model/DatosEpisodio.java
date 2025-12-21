package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosEpisodio(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Episode")Integer numeroEpisodio,
        @JsonAlias("imdbRating")String evaluacion,
        @JsonAlias("Released")String fechaDeLanzamiento
) {
    // DTO para un episodio dentro de la respuesta de una temporada de OMDB.
    // Algunos campos pueden venir como "N/A" y requieren parsing defensivo
    // al mapear a la entidad Episodio.
}
