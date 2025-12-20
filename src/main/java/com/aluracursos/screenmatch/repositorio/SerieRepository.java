package com.aluracursos.screenmatch.repositorio;

import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository <Serie, Long>{
    // Interfaz de repositorio Spring Data JPA: hereda métodos CRUD y de paginación.

    // Devuelve una serie que contiene el texto dado en el título (case-insensitive)
    Optional<Serie> findByTituloContainsIgnoreCase(String titulo);

    // Devuelve las 5 series mejor evaluadas, ordenadas por la propiedad 'evaluacion' desc.
    List<Serie> findTop5ByOrderByEvaluacionDesc();

}
