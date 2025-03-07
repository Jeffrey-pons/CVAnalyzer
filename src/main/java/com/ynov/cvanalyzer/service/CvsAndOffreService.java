package com.ynov.cvanalyzer.service;

import com.ynov.cvanalyzer.dto.CvsAndOffreDto;
import com.ynov.cvanalyzer.entity.Cv;
import com.ynov.cvanalyzer.entity.CvsAndOffre;
import com.ynov.cvanalyzer.repository.CvsAndOffreRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

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
