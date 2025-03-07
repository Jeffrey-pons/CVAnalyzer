package a.ynov.back.service;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import a.ynov.back.dto.CvDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.repository.CvRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class CvService {
    private CvRepository cvRepository;

    public CvService(CvRepository cvRepository) {
        this.cvRepository = cvRepository;

    }

    public Cv save(CvDto cvDto) {
        Cv cv = new Cv();
        cv.setContenu(cvDto.contenu());
        return cvRepository.save(cv);
    }

    public Iterable<Cv> saveAll(List<CvDto> cvDto) {
        List<Cv> cvs = new ArrayList<>();
        for (CvDto cvDto1 : cvDto) {
            cvs.add(save(cvDto1));
        }
        return cvRepository.saveAll(cvs);
    }
}
