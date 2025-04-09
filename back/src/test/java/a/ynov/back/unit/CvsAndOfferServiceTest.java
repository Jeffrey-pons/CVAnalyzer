package a.ynov.back.unit;

import a.ynov.back.dto.CvsAndOfferDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.entity.CvsAndOffer;
import a.ynov.back.entity.Offer;
import a.ynov.back.repository.CvsAndOffreRepository;
import a.ynov.back.service.CvsAndOfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CvsAndOfferServiceTest {

    @Mock
    private CvsAndOffreRepository firstQstRepository;

    private CvsAndOfferService cvsAndOfferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cvsAndOfferService = new CvsAndOfferService(firstQstRepository);
    }

    @Test
    void save_validCvsAndOfferDto() {
        Offer offer = new Offer();
        offer.setId(1);

        Cv cv1 = new Cv();
        cv1.setId(1);
        Cv cv2 = new Cv();
        cv2.setId(2);

        CvsAndOfferDto firstQuestionDto = new CvsAndOfferDto(offer, List.of(cv1, cv2));

        CvsAndOffer firstQuestion = new CvsAndOffer();
        firstQuestion.setOffre(offer);
        firstQuestion.setCvs(List.of(cv1, cv2));

        when(firstQstRepository.save(any(CvsAndOffer.class))).thenReturn(firstQuestion);

        CvsAndOffer savedFirstQuestion = cvsAndOfferService.save(firstQuestionDto);
        verify(firstQstRepository, times(1)).save(any(CvsAndOffer.class));
        assertEquals(offer, savedFirstQuestion.getOffre());
        assertEquals(2, savedFirstQuestion.getCvs().size());
        assertTrue(savedFirstQuestion.getCvs().contains(cv1));
        assertTrue(savedFirstQuestion.getCvs().contains(cv2));
    }

    @Test
    void save_emptyCvsList() {
        Offer offer = new Offer();
        offer.setId(1);

        CvsAndOfferDto firstQuestionDto = new CvsAndOfferDto(offer, List.of()); // Liste vide de CV

        CvsAndOffer firstQuestion = new CvsAndOffer();
        firstQuestion.setOffre(offer);
        firstQuestion.setCvs(List.of());

        when(firstQstRepository.save(any(CvsAndOffer.class))).thenReturn(firstQuestion);

        CvsAndOffer savedFirstQuestion = cvsAndOfferService.save(firstQuestionDto);
        verify(firstQstRepository, times(1)).save(any(CvsAndOffer.class));
        assertEquals(offer, savedFirstQuestion.getOffre());
        assertTrue(savedFirstQuestion.getCvs().isEmpty()); // Liste de CV doit être vide
    }

    @Test
    void save_nullOffer() {
        Offer offer = null;
        Cv cv1 = new Cv();
        cv1.setId(1);

        CvsAndOfferDto firstQuestionDto = new CvsAndOfferDto(offer, List.of(cv1));

        CvsAndOffer firstQuestion = new CvsAndOffer();
        firstQuestion.setOffre(offer);
        firstQuestion.setCvs(List.of(cv1));

        when(firstQstRepository.save(any(CvsAndOffer.class))).thenReturn(firstQuestion);

        CvsAndOffer savedFirstQuestion = cvsAndOfferService.save(firstQuestionDto);
        verify(firstQstRepository, times(1)).save(any(CvsAndOffer.class));
        assertNull(savedFirstQuestion.getOffre()); // Offre doit être nulle
        assertEquals(1, savedFirstQuestion.getCvs().size());
        assertTrue(savedFirstQuestion.getCvs().contains(cv1));
    }

    @Test
    void save_shouldHandleRepositoryException() {
        Offer offer = new Offer();
        offer.setId(1);

        Cv cv1 = new Cv();
        cv1.setId(1);

        CvsAndOfferDto firstQuestionDto = new CvsAndOfferDto(offer, List.of(cv1));

        when(firstQstRepository.save(any(CvsAndOffer.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cvsAndOfferService.save(firstQuestionDto);
        });

        assertEquals("Database error", exception.getMessage());
    }

}
