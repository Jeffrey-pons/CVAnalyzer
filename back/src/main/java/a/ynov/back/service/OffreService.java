package a.ynov.back.service;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import a.ynov.back.dto.OffreDto;
import a.ynov.back.entity.Offre;
import a.ynov.back.repository.OffreRepository;

@Service
@Getter
@Setter
public class OffreService {
    public OffreRepository offreRepository;

    public OffreService(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    public Offre save(OffreDto offreDto) {
        Offre offre = new Offre();
        offre.setDescription(offreDto.description());
        return offreRepository.save(offre);
    }


}
