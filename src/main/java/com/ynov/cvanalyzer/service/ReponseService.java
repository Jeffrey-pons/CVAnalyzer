package com.ynov.cvanalyzer.service;

import com.ynov.cvanalyzer.dto.ReponseDto;
import com.ynov.cvanalyzer.entity.ReponseIA;
import com.ynov.cvanalyzer.repository.ReponseRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class ReponseService {
    private ReponseRepository reponseRepository;
    public ReponseService(ReponseRepository reponseRepository) {
        this.reponseRepository = reponseRepository;
    }

    public ReponseIA save(ReponseDto reponseDto) {
        ReponseIA reponseIA = new ReponseIA();
        reponseIA.setMessage(reponseDto.message());
        reponseIA.setFirstQuestion(reponseDto.firstQuestion());
        return reponseRepository.save(reponseIA);
    }
}
