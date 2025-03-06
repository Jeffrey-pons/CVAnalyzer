package com.ynov.cvanalyzer.controller;

import com.ynov.cvanalyzer.dto.CmpCvsOffreDto;
import com.ynov.cvanalyzer.dto.CvDto;
import com.ynov.cvanalyzer.dto.CvsDto;
import com.ynov.cvanalyzer.dto.OffreDto;
import com.ynov.cvanalyzer.entity.Cv;
import com.ynov.cvanalyzer.entity.Offre;
import com.ynov.cvanalyzer.service.CvService;
import com.ynov.cvanalyzer.service.OffreService;
import com.ynov.cvanalyzer.service.ReadingService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
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
    private final OllamaChatModel chatModel;

    @PostMapping()
    public String CompareCvs(@RequestBody CmpCvsOffreDto cvsOffreDto) {
        //save l'offre dans la base de données
        Offre savedOffre = offreService.save(cvsOffreDto.offreDesc());

        //save la liste des cv dans la base de données
        Iterable<Cv> cvssaved = cvService.saveAll(cvsOffreDto.cvsDto());

        //concaténation de l'offre avec les cv
        String question = cvssaved.toString() + savedOffre.toString();

        String prompt = cvreadingService.readInternalFileAsString("prompts/promptCompareCvs.txt") ;

        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn>")) ;
        messages.add(new UserMessage("<start_of_turn>" + question + "<end_of_turn>")) ;

        Prompt promptToSend = new Prompt(messages);
        Flux<ChatResponse> chatResponses = chatModel.stream(promptToSend);
        String message = Objects.requireNonNull(chatResponses.collectList().block()).stream()
                .map(response -> response.getResult().getOutput().getText())
                .collect(Collectors.joining("")) ;

        return message ;

    }

//    @PostMapping()
//    public Cv Cv(@RequestBody CvDto cvDto){
//        return cvService.save(cvDto);
//    }
}
