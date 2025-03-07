package a.ynov.back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatIAService {
    private final OllamaChatModel chatModel;

    public String sendMessageToIA(List<Message> messages) {
        try {

            Prompt promptToSend = new Prompt(messages);
            Flux<ChatResponse> chatResponses = chatModel.stream(promptToSend);

            // Récupération des réponses de l'IA
            List<ChatResponse> responses = chatResponses.collectList().block();

            // Vérification si la réponse n'est pas vide
            if (responses == null || responses.isEmpty()) {
                log.warn("Aucune réponse reçue de l'IA");
                return "";
            }

            // Concaténation des réponses reçues
            StringBuilder result = new StringBuilder();
            for (ChatResponse response : responses) {
                result.append(response.getResult().getOutput().getText());
            }
            return result.toString();

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi du message à l'IA : {}", e.getMessage(), e);
            return "";
        }
    }
}
