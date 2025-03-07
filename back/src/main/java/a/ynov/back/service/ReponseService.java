package a.ynov.back.service;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import a.ynov.back.dto.ReponseDto;
import a.ynov.back.entity.ReponseIA;
import a.ynov.back.repository.ReponseRepository;

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
