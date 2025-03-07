package a.ynov.back.controller;


import a.ynov.back.dto.CmpCvsOfferDto;
import a.ynov.back.dto.CvsAndOfferDto;
import a.ynov.back.dto.ResponseDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.entity.CvsAndOffer;
import a.ynov.back.entity.Offer;
import a.ynov.back.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
@RequestMapping("cv")
@RequiredArgsConstructor
public class CvController {
    private final CvService cvService;
    private final OfferService offreService;
    private final ReadingService cvreadingService;
    private final ResponseService reponseService;
    private final CvsAndOfferService firstQstService;
    private final ChatIAService chatIAService;
    private final OllamaChatModel chatModel;

    @PostMapping()
    public String CompareCvs(@RequestBody CmpCvsOfferDto cvsOffreDto) {
        //save l'offre dans la base de données
        Offer savedOffre = offreService.save(cvsOffreDto.offreDesc());
        //save la liste des cv dans la base de données
        Iterable<Cv> cvssaved = cvService.saveAll(cvsOffreDto.cvsDto());

        //save de la premiere question
        CvsAndOfferDto firstQstDto = new CvsAndOfferDto(savedOffre,cvssaved);
        CvsAndOffer firstQuestion = firstQstService.save(firstQstDto);

        //concaténation de l'offre avec les cv
        String question = cvssaved.toString() + savedOffre.toString();
        String prompt = cvreadingService.readInternalFileAsString("prompts/promptCompareCvs.txt") ;

        List<Message> messages = List.of(
                new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn>"),
                new UserMessage("<start_of_turn>" + question + "<end_of_turn>")
        );

        String message = chatIAService.sendMessageToIA(messages);
        reponseService.save(new ResponseDto(message, firstQuestion));

        return message;
    }
}
