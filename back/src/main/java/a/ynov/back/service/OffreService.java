package a.ynov.back.service;


import a.ynov.back.dto.OfferDto;
import a.ynov.back.entity.Offer;
import a.ynov.back.repository.OfferRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Service
@Getter
@Setter
public class OffreService {
    public OfferRepository offreRepository;

    public OffreService(OfferRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    public Offer save(OfferDto offreDto) {
        Offer offre = new Offer();
        offre.setDescription(offreDto.description());
        return offreRepository.save(offre);
    }


}
