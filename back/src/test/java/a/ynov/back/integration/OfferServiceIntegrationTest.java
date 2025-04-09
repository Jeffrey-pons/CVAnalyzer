package a.ynov.back.integration;

import a.ynov.back.dto.OfferDto;
import a.ynov.back.entity.Offer;
import a.ynov.back.repository.OfferRepository;
import a.ynov.back.service.OfferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OfferServiceIntegrationTest {

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferRepository offerRepository;

    @Test
    void save_ShouldPersistOfferToDatabase() {
        OfferDto dto = new OfferDto("Software Engineer");
        Offer savedOffer = offerService.save(dto);

        assertNotNull(savedOffer.getId());
        assertEquals("Software Engineer", savedOffer.getDescription());
    }
}
