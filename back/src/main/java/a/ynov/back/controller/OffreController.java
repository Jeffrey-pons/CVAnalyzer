package a.ynov.back.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import a.ynov.back.dto.OffreDto;
import a.ynov.back.entity.Offre;
import a.ynov.back.service.OffreService;
import a.ynov.back.service.ReadingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("offre")
@RequiredArgsConstructor
public class OffreController {
    private final OffreService offreService;
    private final ReadingService offreReadingService;
    private final OllamaChatModel chatModel;

    @PostMapping()
    public String SendOffreToIA(@RequestBody OffreDto offreDto) {
        Offre savedOffre = offreService.save(offreDto);
        String question = savedOffre.toString();

        String prompt = offreReadingService.readInternalFileAsString("prompts/promptConsignes.txt");

        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn>"));
        messages.add(new UserMessage("<start_of_turn>" + question + "<end_of_turn>"));

        Prompt promptToSend = new Prompt(messages);
        Flux<ChatResponse> chatResponses = chatModel.stream(promptToSend);
        String message = Objects.requireNonNull(chatResponses.collectList().block()).stream()
                .map(response -> response.getResult().getOutput().getText())
                .collect(Collectors.joining(""));

        return message;

    }

}
