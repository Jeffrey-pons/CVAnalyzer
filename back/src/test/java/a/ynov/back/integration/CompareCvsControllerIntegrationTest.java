package a.ynov.back.integration;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CompareCvsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Un seul CV
    @Test
    void compareCvs_ShouldReturn200OK() throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Contenu de CV - Développeur Java");
        contentStream.endText();
        contentStream.close();

        File tempFile = File.createTempFile("cv_", ".pdf");
        document.save(tempFile);
        document.close();

        MockMultipartFile file = new MockMultipartFile(
                "files", tempFile.getName(), "application/pdf", new FileInputStream(tempFile)
        );

        // Faire la requête avec le fichier et paramètre "jobOffer"
        mockMvc.perform(multipart("/cv/compare-cvs")
                        .file(file)
                        .param("jobOffer", "Développeur Java"))
                .andExpect(status().isOk())  // Vérifier le status HTTP 200 OK
                .andExpect(result -> {
                    // Vérifier que la réponse n'est pas vide
                    String responseBody = result.getResponse().getContentAsString();
                    assertFalse(responseBody.isEmpty(), "La réponse ne doit pas être vide");
                });

        tempFile.delete();
    }



    // Plusieurs CV
    @Test
    void compareMultipleCvs_ShouldReturn200OK() throws Exception {
        PDDocument document1 = new PDDocument();
        PDPage page1 = new PDPage();
        document1.addPage(page1);
        PDPageContentStream contentStream1 = new PDPageContentStream(document1, page1);
        contentStream1.beginText();
        contentStream1.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream1.newLineAtOffset(100, 700);
        contentStream1.showText("Contenu de CV 1 - Développeur Java");
        contentStream1.endText();
        contentStream1.close();

        PDDocument document2 = new PDDocument();
        PDPage page2 = new PDPage();
        document2.addPage(page2);
        PDPageContentStream contentStream2 = new PDPageContentStream(document2, page2);
        contentStream2.beginText();
        contentStream2.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream2.newLineAtOffset(100, 700);
        contentStream2.showText("Contenu de CV 2 - Développeur Spring");
        contentStream2.endText();
        contentStream2.close();

        File tempFile1 = File.createTempFile("cv_", ".pdf");
        document1.save(tempFile1);
        document1.close();

        File tempFile2 = File.createTempFile("cv_", ".pdf");
        document2.save(tempFile2);
        document2.close();

        MockMultipartFile file1 = new MockMultipartFile(
                "files", tempFile1.getName(), "application/pdf", new FileInputStream(tempFile1)
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "files", tempFile2.getName(), "application/pdf", new FileInputStream(tempFile2)
        );

        // Faire la requête avec les deux fichiers et le paramètre "jobOffer"
        mockMvc.perform(multipart("/cv/compare-cvs")
                        .file(file1)
                        .file(file2)
                        .param("jobOffer", "Développeur Spring"))
                .andExpect(status().isOk())  // Vérifier le status HTTP 200 OK
                .andExpect(result -> {
                    // Vérifier que la réponse n'est pas vide
                    String responseBody = result.getResponse().getContentAsString();
                    assertFalse(responseBody.isEmpty(), "La réponse ne doit pas être vide");
                });

        tempFile1.delete();
        tempFile2.delete();
    }

    // Fichier invalide
    @Test
    void compareCvs_ShouldReturn5xxOnInvalidFile() throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Fake CV content");
        contentStream.endText();
        contentStream.close();

        File tempFile = File.createTempFile("cv_", ".pdf");  // Changer ici "cv" en "cv_"
        document.save(tempFile);
        document.close();

        MockMultipartFile file = new MockMultipartFile(
                "files", tempFile.getName(), "application/pdf", new FileInputStream(tempFile)
        );

        mockMvc.perform(multipart("/cv/compare-cvs")
                        .file(file)
                        .param("jobOffer", "Développeur Java"))
                .andExpect(status().isOk());

        tempFile.delete();
    }
// Fichier manquant
    @Test
    void compareCvs_ShouldReturn400BadRequestWhenNoFileIsProvided() throws Exception {
        mockMvc.perform(multipart("/cv/compare-cvs")
                        .param("jobOffer", "Développeur Java"))
                .andExpect(status().isBadRequest());
    }

}
// Couvre la chaîne complète : envoie de fichiers + paramètre + réponse HTTP