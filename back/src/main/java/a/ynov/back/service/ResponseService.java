package com.ynov.cvanalyzer.service;

import com.ynov.cvanalyzer.dto.ResponseDto;
import com.ynov.cvanalyzer.entity.ResponseIA;
import com.ynov.cvanalyzer.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository reponseRepository;

    public void save(ResponseDto reponseDto) {
        ResponseIA reponseIA = new ResponseIA();
        reponseIA.setMessage(reponseDto.message());
        reponseIA.setFirstQuestion(reponseDto.firstQuestion());

        ResponseIA savedResponse = reponseRepository.save(reponseIA);
    }
}
