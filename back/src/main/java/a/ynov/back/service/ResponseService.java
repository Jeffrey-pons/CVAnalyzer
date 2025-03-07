package a.ynov.back.service;


import a.ynov.back.dto.ResponseDto;
import a.ynov.back.entity.ResponseIA;
import a.ynov.back.repository.ResponseRepository;
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
