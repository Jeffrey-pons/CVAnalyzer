package a.ynov.back.controller;


import a.ynov.back.dto.OfferDto;
import a.ynov.back.entity.Offer;
import a.ynov.back.service.ChatIAService;
import a.ynov.back.service.OfferService;
import a.ynov.back.service.ReadingService;
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
