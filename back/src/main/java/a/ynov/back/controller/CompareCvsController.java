package a.ynov.back.controller;

import a.ynov.back.dto.CvsAndOfferDto;
import a.ynov.back.dto.OfferDto;
import a.ynov.back.dto.ResponseDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.entity.CvsAndOffer;
import a.ynov.back.entity.Offer;
import a.ynov.back.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("cv")
@RequiredArgsConstructor
public class CompareCvsController {
    private final CvService cvService;
    private final OfferService offreService;
    private final ReadingService cvreadingService;
    private final ResponseService reponseService;
    private final CvsAndOfferService firstQstService;
    private final ChatIAService chatIAService;
    private final JpaContext jpaContext;

    @PostMapping("/compare-cvs")
    public String compareCvs(
            @RequestParam("jobOffer") String jobOffer,
            @RequestParam("files") List<MultipartFile> files) throws IOException {

        // Sauvegarder l'offre d'emploi dans la base de données
        OfferDto offerDto = new OfferDto(jobOffer);
        Offer savedOffre = offreService.save(offerDto);

        // Sauvegarder les CV (les fichiers PDF)
        Iterable<Cv> cvssaved = cvService.saveAll(files);

        // Sauvegarder la première question (l'association entre l'offre et les CV)
        CvsAndOfferDto firstQstDto = new CvsAndOfferDto(savedOffre, cvssaved);
        CvsAndOffer firstQuestion = firstQstService.save(firstQstDto);

        // Préparer la question et le prompt pour l'IA
        String question = cvssaved.toString() + savedOffre.toString();
        String prompt = cvreadingService.readInternalFileAsString("prompts/promptCompareCvs.txt");

        // Créer les messages pour l'IA
        List<AbstractMessage> messages = List.of(
                new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn"),
                new UserMessage("<start_of_turn>" + question + "<end_of_turn>")
        );


        // Envoyer la question à l'IA et récupérer la réponse
        String message = chatIAService.sendMessageToIA(messages);

        // Sauvegarder la réponse dans la base de données
        reponseService.save(new ResponseDto(message, firstQuestion));

        return message;
    }
}
