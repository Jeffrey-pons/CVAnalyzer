package a.ynov.back.controller;


import a.ynov.back.dto.CmpCvsOffreDto;
import a.ynov.back.dto.CvsAndOffreDto;
import a.ynov.back.dto.ReponseDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.entity.CvsAndOffre;
import a.ynov.back.entity.Offre;
import a.ynov.back.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cv")
@RequiredArgsConstructor
public class CvController {
    private final CvService cvService;
    private final OffreService offreService;
    private final ReadingService cvreadingService;
    private final ReponseService reponseService;
    private final CvsAndOffreService firstQstService;
    private final OllamaChatModel chatModel;

    @PostMapping()
    public String CompareCvs(@RequestBody CmpCvsOffreDto cvsOffreDto) {
        //save l'offre dans la base de données
        Offre savedOffre = offreService.save(cvsOffreDto.offreDesc());

        //save la liste des cv dans la base de données
        Iterable<Cv> cvssaved = cvService.saveAll(cvsOffreDto.cvsDto());

        //save de la premiere question
        CvsAndOffreDto firstQstDto = new CvsAndOffreDto(savedOffre, cvssaved);
        CvsAndOffre firstQuestion = firstQstService.save(firstQstDto);

        //concaténation de l'offre avec les cv
        String question = cvssaved.toString() + savedOffre.toString();

        String prompt = cvreadingService.readInternalFileAsString("prompts/promptCompareCvs.txt");

        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn>"));
        messages.add(new UserMessage("<start_of_turn>" + question + "<end_of_turn>"));

        Prompt promptToSend = new Prompt(messages);
        Flux<ChatResponse> chatResponses = chatModel.stream(promptToSend);
        String message = Objects.requireNonNull(chatResponses.collectList().block()).stream()
                .map(response -> response.getResult().getOutput().getText())
                .collect(Collectors.joining(""));
        ReponseDto response = new ReponseDto(message, firstQuestion);
        reponseService.save(response);
        return message;

    }

}
