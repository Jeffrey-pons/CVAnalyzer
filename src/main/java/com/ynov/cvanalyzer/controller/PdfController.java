package com.ynov.cvanalyzer.controller;

import com.ynov.cvanalyzer.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class PdfController {
    public static String removeNewLines(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("[\\n\\r]", "");
    }
    @Autowired
    private PdfService pdfService;

    // Endpoint pour télécharger un fichier PDF et extraire le texte
    @PostMapping("/extract-text")
    public ResponseEntity<String> extractTextFromPdf(@RequestParam("file") MultipartFile file) {
        try {
            // Sauvegarde temporairement le fichier sur le disque
            File tempFile = File.createTempFile("upload", ".pdf");
            file.transferTo(tempFile);

            // Appel à notre service pour extraire le texte du PDF
            String text = pdfService.extractTextFromPdf(tempFile.getAbsolutePath());

            // Retourne le texte extrait
            return ResponseEntity.ok(removeNewLines(text));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors de l'extraction du texte : " + e.getMessage());
        }
    }
}
