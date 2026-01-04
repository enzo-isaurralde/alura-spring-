package com.aluracursos.screenmatch.repositorio;

import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.domain.Pageable;
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

    // NOTA: en JPQL no existe 'ILIKE'. Para búsquedas case-insensitive con patrón
    // usamos LOWER(...) y LIKE con CONCAT.
    // Esta consulta busca episodios cuyo título contiene el texto dado (insensible a mayúsculas).
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE LOWER(e.titulo) LIKE LOWER(CONCAT('%', :nombreEpisodio, '%'))")
    List<Episodio> episodiosPorNombre(@Param("nombreEpisodio") String nombreEpisodio);

    // JPQL no soporta 'LIMIT'. Si queremos limitar resultados debemos usar Pageable
    // o usar nativeQuery=true con SQL propio. Aquí dejamos la consulta ordenada
    // por evaluación, y si se desea limitar se puede añadir Pageable en la firma
    // o limitar en la llamada desde el servicio.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC")
    List<Episodio> top5Episodios(@Param("serie") Serie serie);


    // Para obtener lanzamientos más recientes ordenamos las series por la fecha máxima
    // de sus episodios. Nuevamente: no usamos LIMIT aquí, devolvemos la lista ordenada;
    // si se desea sólo un top N, se debe usar Pageable en la firma o recortar en el servicio.
    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX(e.fechaDeLanzamiento) DESC")
    List<Serie> lanzamientosMasRecientes();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numeroTemporada")
    List<Episodio> obtenerTemporadasPorNumero(Long id, Long numeroTemporada);
}
