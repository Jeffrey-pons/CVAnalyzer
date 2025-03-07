package a.ynov.back.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PdfService {

    /**
     * Extrait le texte d'un fichier PDF.
     * @param file le fichier PDF.
     * @return le texte extrait du fichier.
     * @throws IOException si une erreur se produit lors de la lecture du fichier.
     */
    public String extractTextFromPdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }
}
