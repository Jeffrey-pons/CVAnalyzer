package a.ynov.back.service;

import a.ynov.back.dto.CvDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.repository.CvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CvService {

    private final CvRepository cvRepository;
    private final PdfService pdfService; // Service pour extraire le texte du fichier PDF

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
     * Transforme un fichier MultipartFile en CvDto.
     * @param file le fichier PDF à transformer.
     * @return un CvDto contenant le contenu extrait du fichier.
     */
    private CvDto convertToCvDto(MultipartFile file) throws IOException {
        // Extraire le texte du fichier PDF
        String contenu = pdfService.extractTextFromPdf(file);

        // Retourner un CvDto avec le contenu extrait
        return new CvDto(contenu);
    }

    /**
     * Sauvegarde une liste de CVs dans la base de données.
     * @param cvDtos la liste des fichiers PDF à convertir en CVs.
     * @return une liste des entités CV sauvegardées.
     */
    public Iterable<Cv> saveAll(List<MultipartFile> cvDtos) throws IOException {
        // Convertir chaque fichier MultipartFile en un CvDto et sauvegarder
        List<Cv> cvs = cvDtos.stream()
                .map(file -> {
                    try {
                        CvDto cvDto = convertToCvDto(file); // Convertir le fichier en CvDto
                        return save(cvDto); // Sauvegarder l'entité Cv
                    } catch (IOException e) {
                        throw new RuntimeException("Erreur lors de la conversion du fichier", e);
                    }
                })
                .collect(Collectors.toList());

        return cvRepository.saveAll(cvs); // Sauvegarde en une seule opération
    }
}
