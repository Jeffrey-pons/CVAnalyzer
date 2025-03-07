package a.ynov.back.service;


import a.ynov.back.dto.CvDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.repository.CvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CvService {

    private final CvRepository cvRepository;

    /**
     * Sauvegarde un CV dans la base de données.
     * @param cvDto le DTO représentant le CV à sauvegarder.
     * @return l'entité CV sauvegardée.
     */
    public Cv save(CvDto cvDto) {
        if (cvDto == null || cvDto.contenu() == null || cvDto.contenu().isEmpty()) {
            throw new IllegalArgumentException("Le contenu du CV ne peut pas être nul ou vide");
        }

        Cv cv = new Cv();
        cv.setContenu(cvDto.contenu());
        return cvRepository.save(cv);
    }

    /**
     * Sauvegarde une liste de CVs dans la base de données.
     * @param cvDtos la liste des DTOs représentant les CVs.
     * @return une liste des entités CV sauvegardées.
     */
    public List<Cv> saveAll(List<CvDto> cvDtos) {
        if (cvDtos == null || cvDtos.isEmpty()) {
            throw new IllegalArgumentException("La liste des CVs ne peut pas être vide ou nulle");
        }

        List<Cv> cvs = cvDtos.stream()
                .map(this::save) // Transformation des CVs DTO en entités CV
                .collect(Collectors.toList());

        return (List<Cv>) cvRepository.saveAll(cvs); // Sauvegarde en une seule opération
    }
}
