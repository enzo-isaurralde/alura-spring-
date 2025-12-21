package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.principal.Principal;
import com.aluracursos.screenmatch.repositorio.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de Spring Boot que inicia la aplicación y ejecuta
 * el menú CLI (clase Principal) usando el repositorio inyectado.
 *
 * Nota: la ejecución del menú se realiza en el hilo principal y espera
 * entradas por consola; esto es adecuado para aplicaciones de ejemplo/CLI.
 */
@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
    @Autowired
    private SerieRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Se crea la clase "Principal" que contiene la lógica del menú CLI.
		// Usamos el repositorio inyectado para que la capa de presentación
		// pueda persistir las entidades consultadas.
		Principal principal = new Principal(repository);
		principal.muestraElMenu();




	}
}
