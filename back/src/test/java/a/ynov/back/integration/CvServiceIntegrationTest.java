package a.ynov.back.integration;

import a.ynov.back.entity.Cv;
import a.ynov.back.repository.CvRepository;
import a.ynov.back.service.CvService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CvServiceIntegrationTest {

    @Autowired
    private CvService cvService;

    @Autowired
    private CvRepository cvRepository;

    @Test
    void save_ShouldPersistCvInDatabase() {
        Cv cv = cvService.save(new a.ynov.back.dto.CvDto("Integration test content"));
        assertNotNull(cv.getId());
        assertEquals("Integration test content", cv.getContenu());
    }

    @Test
    void saveAll_ShouldExtractTextAndPersistCvs() throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Fake PDF content");
        contentStream.endText();
        contentStream.close();

        File tempFile = File.createTempFile("cv_test", ".pdf");
        document.save(tempFile);
        document.close();

        MockMultipartFile mockFile = new MockMultipartFile(
                "files", tempFile.getName(), "application/pdf", new FileInputStream(tempFile)
        );

        List<Cv> savedCvs = (List<Cv>) cvService.saveAll(List.of(mockFile));

        assertFalse(savedCvs.isEmpty());
        assertNotNull(savedCvs.get(0).getContenu());

        tempFile.delete();
    }
}
