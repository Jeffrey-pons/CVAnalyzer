package a.ynov.back.unit;

import a.ynov.back.controller.CompareCvsController;
import a.ynov.back.dto.CvsAndOfferDto;
import a.ynov.back.dto.OfferDto;
import a.ynov.back.dto.ResponseDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.entity.CvsAndOffer;
import a.ynov.back.entity.Offer;
import a.ynov.back.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CompareCvsControllerTest {

    @InjectMocks
    private CompareCvsController compareCvsController;

    @Mock
    private CvService cvService;

    @Mock
    private OfferService offerService;

    @Mock
    private CvsAndOfferService cvsAndOfferService;

    @Mock
    private ChatIAService chatIAService;

    @Mock
    private ResponseService responseService;

    @Mock
    private ReadingService readingService;

    private Offer mockOffer;
    private CvsAndOffer mockCvsAndOffer;
    private List<Cv> mockCvs;

    @BeforeEach
    void setUp() {
        mockOffer = new Offer();
        mockOffer.setDescription("Java Developer");

        Cv mockCv = new Cv();
        mockCv.setContenu("CV Content");

        mockCvs = List.of(mockCv);

        mockCvsAndOffer = new CvsAndOffer();
        mockCvsAndOffer.setOffre(mockOffer);
        mockCvsAndOffer.setCvs(mockCvs);
    }

    @Test
    void compareCvs_ShouldReturnIAResponse() throws IOException {
        // Mock des services
        when(offerService.save(any(OfferDto.class))).thenReturn(mockOffer);
        when(cvService.saveAll(anyList())).thenReturn(mockCvs);
        when(cvsAndOfferService.save(any(CvsAndOfferDto.class))).thenReturn(mockCvsAndOffer);
        when(readingService.readInternalFileAsString(anyString())).thenReturn("IA Prompt");
        when(chatIAService.sendMessageToIA(anyList())).thenReturn("IA Response");

        // Exécution
        String response = compareCvsController.compareCvs("Java Developer", List.of(mock(MultipartFile.class)));

        // Vérifications
        assertEquals("IA Response", response);
        verify(offerService, times(1)).save(any(OfferDto.class));
        verify(cvService, times(1)).saveAll(anyList());
        verify(chatIAService, times(1)).sendMessageToIA(anyList());
        verify(responseService, times(1)).save(any(ResponseDto.class));
        verify(cvsAndOfferService, times(1)).save(any(CvsAndOfferDto.class));
    }

    @Test
    void compareCvs_ShouldHandleEmptyFiles() throws IOException {
        when(cvService.saveAll(anyList())).thenThrow(new IOException("Invalid PDF"));

        Exception exception = assertThrows(
                IOException.class,
                () -> compareCvsController.compareCvs("Java Developer", List.of(mock(MultipartFile.class)))
        );

        assertEquals("Invalid PDF", exception.getMessage());
    }

    @Test
    void compareCvsEndpoint_ShouldReturn200() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(compareCvsController).build();

        MockMultipartFile file = new MockMultipartFile("files", "cv.pdf", "application/pdf", "mock content".getBytes());

        when(offerService.save(any(OfferDto.class))).thenReturn(mockOffer);
        when(cvService.saveAll(anyList())).thenReturn(mockCvs);
        when(cvsAndOfferService.save(any(CvsAndOfferDto.class))).thenReturn(mockCvsAndOffer);
        when(readingService.readInternalFileAsString(anyString())).thenReturn("IA Prompt");
        when(chatIAService.sendMessageToIA(anyList())).thenReturn("IA Response");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/cv/compare-cvs")
                        .file(file)
                        .param("jobOffer", "Java Developer"))
                .andExpect(status().isOk());

        verify(offerService, times(1)).save(any(OfferDto.class));
        verify(cvService, times(1)).saveAll(anyList());
        verify(chatIAService, times(1)).sendMessageToIA(anyList());
    }

    @Test
    void compareCvs_ShouldHandleIAError() throws IOException {
        when(offerService.save(any(OfferDto.class))).thenReturn(mockOffer);
        when(chatIAService.sendMessageToIA(anyList())).thenThrow(new RuntimeException("IA service error"));

        Exception exception = assertThrows(RuntimeException.class, () ->
                compareCvsController.compareCvs("Java Developer", List.of(mock(MultipartFile.class)))
        );

        assertEquals("IA service error", exception.getMessage());
    }
}
