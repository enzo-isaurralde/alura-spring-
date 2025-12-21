package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repositorio.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que representa la interfaz de línea de comandos (CLI) de la aplicación.
 * Responsable de leer entradas del usuario, invocar servicios para consumir la API
 * OMDB y persistir/consultar entidades mediante el repositorio inyectado.
 *
 * Notas:
 * - Esta clase no realiza validaciones exhaustivas de entrada; se asume uso interactivo.
 * - Todas las operaciones que realizan IO o parsing pueden lanzar RuntimeExceptions
 *   desde las clases auxiliares (ConsumoAPI, ConvierteDatos).
 */
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=bc60d5f";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSerie = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    // El repositorio se inyecta desde la clase de arranque. Esta clase
    // actúa como UI por consola: pide entradas, muestra resultados
    // y persiste entidades.
    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    // Bucle principal del menú CLI. Lee opciones y delega en métodos.
    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por titulo
                    5 - Top 5 series mejor evaluadas
                    6 - Buscar series por género
                    7 - Buscar series filtradas por temporadas y evaluación

                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    // Solicita el nombre de la serie al usuario, realiza la llamada HTTP
    // a la API OMDB y convierte el JSON resultante a un objeto DatosSerie.
    // Puntos de fallo:
    // - La llamada HTTP puede lanzar runtime exceptions en caso de fallo de red.
    // - El JSON podría tener campos faltantes o diferentes; el conversor
    //   usa Jackson y mapeará según las anotaciones de record DatosSerie.
    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    // Permite buscar episodios para una serie previamente guardada en la base de datos.
    // Flujo:
    // 1. Muestra las series guardadas
    // 2. El usuario indica una serie por nombre (búsqueda por contains)
    // 3. Por cada temporada, consulta la API con el parámetro &season=i
    // 4. Convierte la respuesta en DatosTemporadas y agrega los episodios
    // 5. Persiste la entidad Serie actualizada (con episodios)
    // Puntos de atención:
    // - 'series' debe no ser null; se carga en mostrarSeriesBuscadas().
    // - Si hay discrepancias en títulos o problemas de conexión las listas
    //   de temporadas pueden quedar vacías.
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);


        }

    }

    // Busca una serie en la web (OMDB) usando getDatosSerie(), crea una entidad Serie
    // desde el DTO y la persiste. Imprime el DTO recibido para inspección.
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        System.out.println(datos);
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    // Recupera todas las series guardadas y las muestra ordenadas por género.
    // Importante: actualiza el campo 'series' usado por otros métodos.
    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        // Usamos Comparator.nullsLast para evitar NPE si alguna serie tiene genero == null
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero, Comparator.nullsLast(Comparator.naturalOrder())))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escribe el nombre de la serie de la cual que desea buscar");
        var nombreSerie = teclado.nextLine();
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (serieBuscada.isPresent()) {
            System.out.println("La serie buscada es: " + serieBuscada.get());
        } else {
            System.out.println("No se encontró ninguna serie con ese título.");
        }
    }
    private void buscarTop5Series() {
        // Llamada al metodo generado por Spring Data JPA para obtener el top 5
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        System.out.println("Top 5 series mejor evaluadas:");
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + ", Evaluación: " + s.getEvaluacion()));
    }
    private void buscarSeriesPorCategoria(){
        System.out.println("Escribe el genero de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Series encontradas en la categoría " + categoria + ":");
        seriesPorCategoria.forEach(System.out::println);
    }
    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Com evaluación apartir de cuál valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemparadaYEvaluacion(totalTemporadas,evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }

}
