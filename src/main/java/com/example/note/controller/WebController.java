package com.example.note.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    private final ResourceLoader resourceLoader;

    public WebController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/")
    public ResponseEntity<Resource> index() {
        return serveHtmlFile("index.html");
    }

    @GetMapping("/parametres")
    public ResponseEntity<Resource> parametres() {
        return serveHtmlFile("parametres.html");
    }

    @GetMapping("/operateurs")
    public ResponseEntity<Resource> operateurs() {
        return serveHtmlFile("operateurs.html");
    }

    @GetMapping("/resolutions")
    public ResponseEntity<Resource> resolutions() {
        return serveHtmlFile("resolutions.html");
    }

    @GetMapping("/notes")
    public ResponseEntity<Resource> notes() {
        return serveHtmlFile("notes.html");
    }

    @GetMapping("/candidats")
    public ResponseEntity<Resource> candidats() {
        return serveHtmlFile("candidats.html");
    }

    @GetMapping("/correcteurs")
    public ResponseEntity<Resource> correcteurs() {
        return serveHtmlFile("correcteurs.html");
    }

    @GetMapping("/matieres")
    public ResponseEntity<Resource> matieres() {
        return serveHtmlFile("matieres.html");
    }

    private ResponseEntity<Resource> serveHtmlFile(String filename) {
        try {
            Resource resource = resourceLoader.getResource("classpath:static/" + filename);
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(resource);
            } else {
                // Fallback : chercher dans le répertoire racine
                Resource fallbackResource = resourceLoader.getResource("file:" + filename);
                if (fallbackResource.exists()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.TEXT_HTML)
                            .body(fallbackResource);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }
}
