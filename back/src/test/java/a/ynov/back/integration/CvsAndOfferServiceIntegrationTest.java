package a.ynov.back.integration;

import a.ynov.back.dto.CvsAndOfferDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.entity.CvsAndOffer;
import a.ynov.back.entity.Offer;
import a.ynov.back.repository.CvsAndOffreRepository;
import a.ynov.back.repository.CvRepository;
import a.ynov.back.repository.OfferRepository;
import a.ynov.back.service.CvsAndOfferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CvsAndOfferServiceIntegrationTest {

    @Autowired
    private CvsAndOfferService cvsAndOfferService;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private CvsAndOffreRepository cvsAndOffreRepository;
// 1 offre & 2 CV sauvegarder / relié
    @Test
    void save_ShouldPersistCvsAndOffer() {
        // Création de l'offre
        Offer offer = new Offer();
        offer.setDescription("Développeur Fullstack");
        offer = offerRepository.save(offer);

        // Création des CVs
        Cv cv1 = new Cv();
        cv1.setContenu("Contenu CV 1");
        cv1 = cvRepository.save(cv1);

        Cv cv2 = new Cv();
        cv2.setContenu("Contenu CV 2");
        cv2 = cvRepository.save(cv2);

        CvsAndOfferDto dto = new CvsAndOfferDto(offer, List.of(cv1, cv2));

        CvsAndOffer saved = cvsAndOfferService.save(dto);

        assertNotNull(saved.getId());
        assertEquals(offer.getId(), saved.getOffre().getId());
        assertEquals(2, saved.getCvs().size());
    }
// Persistance offre sans CV
    @Test
    void save_ShouldNotSaveCvsAndOfferWhenNoCvsAreProvided() {
        // Création de l'offre
        Offer offer = new Offer();
        offer.setDescription("Développeur Fullstack");
        offer = offerRepository.save(offer);

        CvsAndOfferDto dto = new CvsAndOfferDto(offer, List.of());

        CvsAndOffer saved = cvsAndOfferService.save(dto);

        assertNotNull(saved.getId());
        assertEquals(offer.getId(), saved.getOffre().getId());
        assertTrue(saved.getCvs().isEmpty());
    }
    // Persistance CV sans offre
    @Test
    void save_ShouldNotSaveCvsWithEmptyContent() {
        // Création de l'offre
        Offer offer = new Offer();
        offer.setDescription("Développeur Fullstack");
        offer = offerRepository.save(offer);

        // Création du CV vide
        Cv emptyCv = new Cv();
        emptyCv.setContenu("");
        emptyCv = cvRepository.save(emptyCv);

        CvsAndOfferDto dto = new CvsAndOfferDto(offer, List.of(emptyCv));

        CvsAndOffer saved = cvsAndOfferService.save(dto);

        assertNotNull(saved.getId());
        assertEquals(offer.getId(), saved.getOffre().getId());
        assertEquals(1, saved.getCvs().size());
        assertEquals("", saved.getCvs().get(0).getContenu());
    }


}
