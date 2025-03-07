package com.ynov.cvanalyzer.controller;

import com.ynov.cvanalyzer.dto.OfferDto;
import com.ynov.cvanalyzer.entity.Offer;
import com.ynov.cvanalyzer.service.ChatIAService;
import com.ynov.cvanalyzer.service.ReadingService;
import com.ynov.cvanalyzer.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("offre")
@RequiredArgsConstructor
public class OffreController {
    private final OfferService offreService;
    private final ReadingService offreReadingService;
    private final OllamaChatModel chatModel;
    private final ChatIAService chatService;

    @PostMapping()
    public String SendOffreToIA(@RequestBody OfferDto offreDto) {
        Offer savedOffre = offreService.save(offreDto);
        String question = savedOffre.toString();
        String prompt = offreReadingService.readInternalFileAsString("prompts/promptConsignes.txt") ;

        List<Message> messages = List.of(
                new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn>"),
                new UserMessage("<start_of_turn>" + question + "<end_of_turn>")
        );

        return chatService.sendMessageToIA(messages);
    }
}
