package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosTemporadas(
        @JsonAlias("Season") Integer numero,
        @JsonAlias("Episodes") List<DatosEpisodio> episodios
) {
    // Representa la respuesta JSON de OMDB para una temporada: número y lista de episodios.
    // Jackson mapeará la lista de objetos 'Episodes' al tipo DatosEpisodio.
}
