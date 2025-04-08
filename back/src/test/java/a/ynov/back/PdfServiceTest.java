package a.ynov.back;

import a.ynov.back.service.PdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PdfServiceTest {

    private final PdfService pdfService = new PdfService();

    @Test
    void extractTextFromPdf_ShouldReturnExpectedText() throws IOException {
        String expectedText = "Hello PDF! Testons ensemble si l'exctraction se fait correctement";
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(expectedText);
            contentStream.endText();
            contentStream.close();

            document.save(pdfOutputStream);
        }

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", pdfOutputStream.toByteArray());
        String extractedText = pdfService.extractTextFromPdf(mockFile);

        assertTrue(extractedText.contains(expectedText));
    }

    @Test
    void extractTextFromPdf_ShouldThrowIOException_WhenFileIsInvalid() {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", "invalid.pdf", "application/pdf", new byte[]{0x00, 0x01}); // Contenu non PDF

        assertThrows(IOException.class, () -> pdfService.extractTextFromPdf(invalidFile));
    }
}
