package a.ynov.back.service;

import a.ynov.back.dto.CvsAndOfferDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.entity.CvsAndOffer;
import a.ynov.back.repository.CvsAndOffreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CvsAndOfferService {

    private final CvsAndOffreRepository  firstQstRepository;

    /**
     * Sauvegarde la première question contenant l'offre et les CV.
     *
     * @param firstQuestionDto le DTO contenant l'offre et la liste des CVs.
     * @return l'entité CvsAndOffre sauvegardée.
     */
    public CvsAndOffer save(CvsAndOfferDto firstQuestionDto) {
        // Construction de l'entité à partir du DTO
        CvsAndOffer firstQuestion = new CvsAndOffer();
        firstQuestion.setOffre(firstQuestionDto.offre());
        firstQuestion.setCvs((List<Cv>) firstQuestionDto.cvs());

        // Sauvegarde dans la base de données via le repository
        return firstQstRepository.save(firstQuestion);
    }
}
