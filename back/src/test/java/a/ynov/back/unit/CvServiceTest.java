package a.ynov.back.unit;

import a.ynov.back.dto.CvDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.repository.CvRepository;
import a.ynov.back.service.CvService;
import a.ynov.back.service.PdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CvServiceTest {

    @InjectMocks
    private CvService cvService;

    @Mock
    private CvRepository cvRepository;

    @Mock
    private PdfService pdfService;

    @Test
    void save_ShouldReturnSavedCv() {
        CvDto cvDto = new CvDto("Text Content");
        Cv cv = new Cv();
        cv.setContenu(cvDto.contenu());

        when(cvRepository.save(any(Cv.class))).thenReturn(cv);

        Cv savedCv = cvService.save(cvDto);

        assertEquals("Text Content", savedCv.getContenu());
        verify(cvRepository, times(1)).save(any(Cv.class));
    }

    @Test
    void saveAll_ShouldConvertAndSaveFiles() throws IOException {
        // Mock MultipartFile
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.pdf");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("dummy content".getBytes()));

        when(pdfService.extractTextFromPdf(file)).thenReturn("Extracted Text");

        Cv extractedCv = new Cv();
        extractedCv.setContenu("Extracted Text");

        when(cvRepository.saveAll(anyList())).thenReturn(List.of(extractedCv));

        Iterable<Cv> savedCvs = cvService.saveAll(List.of(file));

        assertEquals("Extracted Text", savedCvs.iterator().next().getContenu());
        verify(cvRepository, times(1)).saveAll(anyList());
    }
}
