package com.aluracursos.screenmatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SerieController {
    @GetMapping("/serie")
    public String mostrarMensaje() {
        return "¡Hola desde SerieController!";
    }
}

