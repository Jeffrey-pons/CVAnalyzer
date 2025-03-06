package com.ynov.cvanalyzer.service;

import com.ynov.cvanalyzer.dto.OffreDto;
import com.ynov.cvanalyzer.entity.Offre;
import com.ynov.cvanalyzer.repository.OffreRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Getter @Setter
public class OffreService {
        public  OffreRepository offreRepository;
        public OffreService( OffreRepository offreRepository) {
            this.offreRepository = offreRepository;
        }

        public Offre save(OffreDto offreDto){
                Offre offre = new Offre();
                offre.setDescription(offreDto.description());
                return offreRepository.save(offre);
        }


}
