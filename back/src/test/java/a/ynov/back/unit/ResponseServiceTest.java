package a.ynov.back.unit;

import a.ynov.back.dto.ResponseDto;
import a.ynov.back.entity.CvsAndOffer;
import a.ynov.back.entity.ResponseIA;
import a.ynov.back.entity.Offer;
import a.ynov.back.entity.Cv;
import a.ynov.back.repository.ResponseRepository;
import a.ynov.back.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ResponseServiceTest {

    @Mock
    private ResponseRepository responseRepository;

    private ResponseService responseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        responseService = new ResponseService(responseRepository);
    }

    @Test
    void save() {
        // Given
        String message = "Test message";

        Offer offer = new Offer();
        offer.setId(1);

        Cv cv1 = new Cv();
        cv1.setId(1); // Initialisation de CV1 avec un ID
        Cv cv2 = new Cv();
        cv2.setId(2); // Initialisation de CV2 avec un ID

        CvsAndOffer firstQuestion = new CvsAndOffer();
        firstQuestion.setOffre(offer);
        firstQuestion.setCvs(Arrays.asList(cv1, cv2));

        ResponseDto responseDto = new ResponseDto(message, firstQuestion);

        ResponseIA responseIA = new ResponseIA();
        responseIA.setMessage(message);
        responseIA.setFirstQuestion(firstQuestion);

        when(responseRepository.save(any(ResponseIA.class))).thenReturn(responseIA);

        responseService.save(responseDto);
        verify(responseRepository, times(1)).save(any(ResponseIA.class));

        ArgumentCaptor<ResponseIA> captor = ArgumentCaptor.forClass(ResponseIA.class);
        verify(responseRepository).save(captor.capture());

        ResponseIA savedResponse = captor.getValue();
        assertEquals(message, savedResponse.getMessage());
        assertEquals(firstQuestion, savedResponse.getFirstQuestion());  // Vérifiez que firstQuestion est correctement affecté
    }

    @Test
    void save_withNullMessage() {
        String message = null;
        String firstQuestion = "What is your name?";

        Offer offer = new Offer();
        offer.setId(1); // Initialisation d'une offre avec un ID

        Cv cv1 = new Cv();
        cv1.setId(1);
        Cv cv2 = new Cv();
        cv2.setId(2);

        CvsAndOffer firstQuestionObj = new CvsAndOffer();
        firstQuestionObj.setOffre(offer);
        firstQuestionObj.setCvs(Arrays.asList(cv1, cv2));

        ResponseDto responseDto = new ResponseDto(message, firstQuestionObj);

        ResponseIA responseIA = new ResponseIA();
        responseIA.setMessage(message);
        responseIA.setFirstQuestion(firstQuestionObj);

        when(responseRepository.save(any(ResponseIA.class))).thenReturn(responseIA);

        responseService.save(responseDto);
        verify(responseRepository, times(1)).save(any(ResponseIA.class));

        ArgumentCaptor<ResponseIA> captor = ArgumentCaptor.forClass(ResponseIA.class);
        verify(responseRepository).save(captor.capture());

        ResponseIA savedResponse = captor.getValue();
        assertNull(savedResponse.getMessage()); // message null should be set
        assertEquals(firstQuestionObj, savedResponse.getFirstQuestion());
    }

    @Test
    void save_withNullFirstQuestion() {
        // Given
        String message = "Test message";
        CvsAndOffer firstQuestion = null;

        ResponseDto responseDto = new ResponseDto(message, firstQuestion);

        ResponseIA responseIA = new ResponseIA();
        responseIA.setMessage(message);
        responseIA.setFirstQuestion(firstQuestion);

        when(responseRepository.save(any(ResponseIA.class))).thenReturn(responseIA);

        responseService.save(responseDto);
        verify(responseRepository, times(1)).save(any(ResponseIA.class));

        ArgumentCaptor<ResponseIA> captor = ArgumentCaptor.forClass(ResponseIA.class);
        verify(responseRepository).save(captor.capture());

        ResponseIA savedResponse = captor.getValue();
        assertEquals(message, savedResponse.getMessage());
        assertNull(savedResponse.getFirstQuestion()); // firstQuestion null should be set
    }

    @Test
    void save_shouldHandleRepositoryException() {
        // Given
        String message = "Test message";
        CvsAndOffer firstQuestion = new CvsAndOffer();

        ResponseDto responseDto = new ResponseDto(message, firstQuestion);

        when(responseRepository.save(any(ResponseIA.class))).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            responseService.save(responseDto);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void save_withLongStrings() {
        // Given
        String longMessage = "A".repeat(10000);  // Une chaîne de 10 000 caractères
        String longQuestion = "B".repeat(10000);

        Offer offer = new Offer();
        offer.setId(1);

        Cv cv1 = new Cv();
        cv1.setId(1);

        CvsAndOffer firstQuestion = new CvsAndOffer();
        firstQuestion.setOffre(offer);
        firstQuestion.setCvs(Arrays.asList(cv1));

        ResponseDto responseDto = new ResponseDto(longMessage, firstQuestion);

        ResponseIA responseIA = new ResponseIA();
        responseIA.setMessage(longMessage);
        responseIA.setFirstQuestion(firstQuestion);

        when(responseRepository.save(any(ResponseIA.class))).thenReturn(responseIA);

        // When
        responseService.save(responseDto);

        // Then
        verify(responseRepository, times(1)).save(any(ResponseIA.class));

        ArgumentCaptor<ResponseIA> captor = ArgumentCaptor.forClass(ResponseIA.class);
        verify(responseRepository).save(captor.capture());

        ResponseIA savedResponse = captor.getValue();
        assertEquals(longMessage, savedResponse.getMessage());
        assertEquals(firstQuestion, savedResponse.getFirstQuestion());
    }

    @Test
    void save_withEmptyResponseDto() {
        // Given
        String message = "";
        CvsAndOffer firstQuestion = null;

        ResponseDto responseDto = new ResponseDto(message, firstQuestion);

        ResponseIA responseIA = new ResponseIA();
        responseIA.setMessage(message);
        responseIA.setFirstQuestion(firstQuestion);

        when(responseRepository.save(any(ResponseIA.class))).thenReturn(responseIA);

        // When
        responseService.save(responseDto);

        // Then
        verify(responseRepository, times(1)).save(any(ResponseIA.class));

        ArgumentCaptor<ResponseIA> captor = ArgumentCaptor.forClass(ResponseIA.class);
        verify(responseRepository).save(captor.capture());

        ResponseIA savedResponse = captor.getValue();
        assertEquals(message, savedResponse.getMessage());
        assertNull(savedResponse.getFirstQuestion());
    }

    @Test
    void save_withEmptyCvsAndOffer() {
        String message = "Test message";
        CvsAndOffer firstQuestion = new CvsAndOffer();

        ResponseDto responseDto = new ResponseDto(message, firstQuestion);

        ResponseIA responseIA = new ResponseIA();
        responseIA.setMessage(message);
        responseIA.setFirstQuestion(firstQuestion);

        when(responseRepository.save(any(ResponseIA.class))).thenReturn(responseIA);

        responseService.save(responseDto);
        verify(responseRepository, times(1)).save(any(ResponseIA.class));

        ArgumentCaptor<ResponseIA> captor = ArgumentCaptor.forClass(ResponseIA.class);
        verify(responseRepository).save(captor.capture());

        ResponseIA savedResponse = captor.getValue();
        assertEquals(message, savedResponse.getMessage());
        assertNotNull(savedResponse.getFirstQuestion());
        assertNull(savedResponse.getFirstQuestion().getOffre());
        assertNull(savedResponse.getFirstQuestion().getCvs());
    }


}
