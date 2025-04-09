package a.ynov.back.unit;

import a.ynov.back.dto.OfferDto;
import a.ynov.back.entity.Offer;
import a.ynov.back.repository.OfferRepository;
import a.ynov.back.service.OfferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @InjectMocks
    private OfferService offerService;

    @Mock
    private OfferRepository offerRepository;

    @Test
    void save_ShouldReturnSavedOffer() {
        OfferDto dto = new OfferDto("Software Engineer");
        Offer offer = new Offer();
        offer.setDescription(dto.description());

        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        Offer savedOffer = offerService.save(dto);

        assertEquals("Software Engineer", savedOffer.getDescription());
        verify(offerRepository, times(1)).save(any(Offer.class));
    }
}
