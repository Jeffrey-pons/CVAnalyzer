package a.ynov.back.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class PdfService {

    /**
     * Extrait le texte d'un fichier PDF.
     *
     * @param filePath chemin du fichier PDF.
     * @return texte extrait du PDF, ou un message d'erreur si une exception est levée.
     */
    public String extractTextFromPdf(String filePath) {
        File file = new File(filePath);

        // Vérification que le fichier existe
        if (!file.exists() || !file.isFile()) {
            return "Le fichier PDF spécifié n'existe pas ou n'est pas valide.";
        }

        try (PDDocument document = PDDocument.load(file)) {
            // Vérification si le document est valide et non corrompu
            if (document.isEncrypted()) {
                return "Le fichier PDF est chiffré et ne peut pas être lu.";
            }

            // Extraction du texte avec PDFTextStripper
            PDFTextStripper stripper = new PDFTextStripper();
            String extractedText = stripper.getText(document);

            // Vérifier si du texte a bien été extrait
            if (extractedText == null || extractedText.trim().isEmpty()) {
                return "Aucun texte n'a pu être extrait du fichier PDF.";
            }

            return extractedText;

        } catch (IOException e) {
            return "Une erreur s'est produite lors de l'extraction du texte : " + e.getMessage();
        }
    }

}
