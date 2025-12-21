package com.aluracursos.screenmatch.repositorio;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository <Serie, Long>{
    // Interfaz de repositorio Spring Data JPA: hereda métodos CRUD y de paginación.

    // Devuelve una serie que contiene el texto dado en el título (case-insensitive)
    Optional<Serie> findByTituloContainsIgnoreCase(String titulo);

    // Devuelve las 5 series mejor evaluadas, ordenadas por la propiedad 'evaluacion' desc.
    List<Serie> findTop5ByOrderByEvaluacionDesc();

    // Devuelve series que coinciden con la categoría dada.
    List<Serie> findByGenero(Categoria categoria);

    // JPQL estándar: aquí se usa una consulta simple para filtrar por temporadas y evaluación.
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemparadaYEvaluacion(int totalTemporadas, Double evaluacion);

    // Atención: JPQL no soporta la palabra reservada ILIKE ni la sintaxis LIMIT de SQL.
    // Si se desea hacer case-insensitive con patrones en JPQL, usar LOWER(e.titulo) LIKE LOWER(CONCAT('%', :nombreEpisodio, '%'))
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    // Atención: 'LIMIT' y 'ORDER BY' con límite no forman parte de JPQL estándar.
    // Para obtener los top 5 episodios sería preferible usar:
    // @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC")
    // y luego limitar en la invocación (p. ej. Pageable) o usar nativeQuery=true con SQL propio.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5 ")
    List<Episodio> top5Episodios(Serie serie);
}
