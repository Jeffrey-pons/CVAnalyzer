package a.ynov.back.service;

import a.ynov.back.dto.CvDto;
import a.ynov.back.entity.Conversation;
import a.ynov.back.entity.Cv;
import a.ynov.back.repository.CvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
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
    public Iterable<Cv> saveAll(List<MultipartFile> files) {
        List<Cv> cvs = files.stream().map(file -> {
            try {
                String contenu = pdfService.extractTextFromPdf(file);

                Cv cv = new Cv();
                cv.setContenu(contenu);
                cv.setOriginalFilename(file.getOriginalFilename());

                String storagePath = System.getProperty("user.dir") + "/uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
                Files.createDirectories(Path.of(System.getProperty("user.dir") + "/uploads/"));
                Files.copy(file.getInputStream(), Path.of(storagePath), StandardCopyOption.REPLACE_EXISTING);
                cv.setStoragePath(storagePath);

                return cv;
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du traitement du fichier : " + file.getOriginalFilename(), e);
            }
        }).collect(Collectors.toList());

        return cvRepository.saveAll(cvs);
    }


    public Iterable<Cv> updateCvsWithConversation(List<Cv> cvs, Conversation conversation) {
        for (Cv cv : cvs) {
            cv.setConversation(conversation);
        }
        return cvRepository.saveAll(cvs);
    }
    public List<Cv> findByConversationId(Long conversationId) {
        return cvRepository.findByConversationId(conversationId);
    }


    public Cv findById(Long id) {
        return cvRepository.findById(id).orElse(null);
    }

    public void deleteAll(List<Cv> cvs) {
        cvRepository.deleteAll(cvs);
    }

}
