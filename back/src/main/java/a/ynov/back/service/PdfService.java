package a.ynov.back.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfService {
    public String extractTextFromPdf(String filePath) throws IOException {
        // Charger le fichier PDF
        File file = new File(filePath);
        try (PDDocument document = PDDocument.load(file)) {
            // Extraire le texte avec PDFTextStripper
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document); // Retourne le texte extrait
        }
    }
}
