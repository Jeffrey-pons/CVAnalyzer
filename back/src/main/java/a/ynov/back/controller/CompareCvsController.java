package a.ynov.back.controller;

import a.ynov.back.dto.CvsAndOfferDto;
import a.ynov.back.dto.OfferDto;
import a.ynov.back.dto.ResponseDto;
import a.ynov.back.entity.Cv;
import a.ynov.back.entity.CvsAndOffer;
import a.ynov.back.entity.Message;
import a.ynov.back.entity.Offer;
import a.ynov.back.repository.ConversationRepository;
import a.ynov.back.repository.MessageRepository;
import a.ynov.back.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
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
    private final ConversationRepository conversationRepo;
    private final MessageRepository messageRepo;

    @PostMapping("/compare-cvs")
    public ResponseEntity<String> compareCvs(
            @RequestParam("jobOffer") String jobOffer,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "systemPrompt", required = false) String systemPrompt) {

        try {
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
            String prompt = (systemPrompt != null && !systemPrompt.isBlank())
                    ? systemPrompt
                    : cvreadingService.readInternalFileAsString("prompts/promptCompareCvs.txt");

            List<AbstractMessage> messages = List.of(
                    new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn>"),
                    new UserMessage("<start_of_turn>" + question + "<end_of_turn>")
            );

            // Appeler l’IA
            String message = chatIAService.sendMessageToIA(messages);

            // Sauvegarder la réponse
            reponseService.save(new ResponseDto(message, firstQuestion));

            return ResponseEntity.ok(message);

        } catch (Exception e) {
            e.printStackTrace(); // pour logs serveur
            return ResponseEntity.status(500).body("Erreur côté serveur : " + e.getMessage());
        }
    }


    @PostMapping("/follow-up")
    public ResponseEntity<String> followUpConversation(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String customPrompt = request.getOrDefault("systemPrompt", "");

        if (userMessage == null || userMessage.isBlank()) {
            return ResponseEntity.badRequest().body("❌ La question ne peut pas être vide.");
        }

        // On peut optionnellement ajouter un message système pour encadrer le contexte
        //String prompt = cvreadingService.readInternalFileAsString("prompts/promptChatbot.txt");
        String prompt = customPrompt.isBlank()
                ? cvreadingService.readInternalFileAsString("prompts/promptChatbot.txt")
                : customPrompt;
        // Crée les messages pour l'IA
        List<AbstractMessage> messages = List.of(
                new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn>"),
                new UserMessage("<start_of_turn>" + userMessage + "<end_of_turn>")
        );

        // Appelle l’IA
        String aiResponse = chatIAService.sendMessageToIA(messages);

        return ResponseEntity.ok(aiResponse);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<Map<String, Object>>> getAllConversations() {
        List<Map<String, Object>> result = conversationRepo.findAll().stream().map(conv -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", conv.getId());
            map.put("title", conv.getTitle());
            map.put("date", conv.getCreatedAt());
            return map;
        }).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/conversations/{id}")
    public ResponseEntity<Map<String, Object>> getConversation(@PathVariable Long id) {
        List<Message> msgs = messageRepo.findByConversationIdOrderByTimestampAsc(id);
        List<Map<String, String>> messages = msgs.stream().map(msg -> {
            Map<String, String> map = new HashMap<>();
            map.put("sender", msg.getSender());
            map.put("text", msg.getContent());
            return map;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("messages", messages);
        return ResponseEntity.ok(response);
    }



}
