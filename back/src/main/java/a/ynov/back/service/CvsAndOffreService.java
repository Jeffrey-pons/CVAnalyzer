package a.ynov.back.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import a.ynov.back.dto.CvsAndOffreDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.entity.CvsAndOffre;
import a.ynov.back.repository.CvsAndOffreRepository;

import java.util.List;

@Service
@Getter
@Setter
public class CvsAndOffreService {
    private CvsAndOffreRepository firstQstRepository;

    public CvsAndOffreService(CvsAndOffreRepository firstQstRepository) {
        this.firstQstRepository = firstQstRepository;
    }

    public CvsAndOffre save(CvsAndOffreDto firstQuestionDto) {
        CvsAndOffre firstQuestion = new CvsAndOffre();
        firstQuestion.setOffre(firstQuestionDto.offre());
        firstQuestion.setCvs((List<Cv>) firstQuestionDto.cvs());
        return firstQstRepository.save(firstQuestion);
    }
}
