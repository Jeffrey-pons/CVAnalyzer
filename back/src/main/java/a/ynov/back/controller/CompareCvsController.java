package a.ynov.back.controller;

import a.ynov.back.dto.CvsAndOfferDto;
import a.ynov.back.dto.OfferDto;
import a.ynov.back.dto.ResponseDto;
import a.ynov.back.entity.*;
import a.ynov.back.repository.ConversationRepository;
import a.ynov.back.repository.MessageRepository;
import a.ynov.back.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public ResponseEntity<Map<String, Object>> compareCvs(
            @RequestParam("jobOffer") String jobOffer,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "systemPrompt", required = false) String systemPrompt) {


        if (files.isEmpty() || files.stream().anyMatch(file -> file.isEmpty())) {
            throw new IllegalArgumentException("Les fichiers envoyés sont vides.");
        }

        try {
            // 1. Sauvegarder l'offre d'emploi
            OfferDto offerDto = new OfferDto(jobOffer);
            Offer savedOffre = offreService.save(offerDto);

            // 2. Sauvegarder les CV
            Iterable<Cv> cvssaved = cvService.saveAll(files);

            // 3. Sauvegarder l'association Offre + CVs
            CvsAndOfferDto firstQstDto = new CvsAndOfferDto(savedOffre, cvssaved);
            CvsAndOffer firstQuestion = firstQstService.save(firstQstDto);

            // 4. Construire la question IA
            //String question = cvssaved.toString() + savedOffre.toString();
            // Question complète pour l'IA
            String questionForIA = cvssaved.toString() + savedOffre.toString();

// Questi   on visible pour l'utilisateur (UNIQUEMENT l'offre)
            String visibleQuestion = savedOffre.toString();
            String prompt = (systemPrompt != null && !systemPrompt.isBlank())
                    ? systemPrompt
                    : cvreadingService.readInternalFileAsString("prompts/promptCompareCvs.txt");

            List<AbstractMessage> messages = List.of(
                    new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn>"),
                    new UserMessage("<start_of_turn>" + questionForIA + "<end_of_turn>")
            );

            // 5. Appeler l'IA
            String aiResponse = chatIAService.sendMessageToIA(messages);
            String conversationTitle = jobOffer.length() > 100 ? jobOffer.substring(0, 100) + "..." : jobOffer;

            // 6. Créer une nouvelle conversation
            Conversation conversation = new Conversation();
            conversation.setTitle(conversationTitle);
            conversation.setCreatedAt(LocalDateTime.now());
            conversation = conversationRepo.save(conversation);
            List<Cv> cvList = new ArrayList<>();
            cvssaved.forEach(cvList::add);

            cvService.updateCvsWithConversation(cvList, conversation);



            // 7. Sauvegarder les messages (utilisateur + IA)
            Message userMsg = new Message();
            userMsg.setConversation(conversation);
            userMsg.setSender("user");
            userMsg.setContent(visibleQuestion);
            userMsg.setTimestamp(LocalDateTime.now());

            Message aiMsg = new Message();
            aiMsg.setConversation(conversation);
            aiMsg.setSender("ai");
            aiMsg.setContent(aiResponse);
            aiMsg.setTimestamp(LocalDateTime.now());

            messageRepo.save(userMsg);
            messageRepo.save(aiMsg);

            // 8. Sauvegarder la réponse liée à CvsAndOffer
            reponseService.save(new ResponseDto(aiResponse, firstQuestion));

            // 9. Préparer la réponse pour le frontend
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", aiResponse);
            responseMap.put("conversationId", conversation.getId());

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Erreur côté serveur : " + e.getMessage());
            return ResponseEntity.status(500).body(errorMap);
        }
    }





    @PostMapping("/follow-up/{conversationId}")
    public ResponseEntity<String> followUpConversation(@PathVariable Long conversationId, @RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String customPrompt = request.getOrDefault("systemPrompt", "");

        if (userMessage == null || userMessage.isBlank()) {
            return ResponseEntity.badRequest().body("❌ La question ne peut pas être vide.");
        }

        var conversationOpt = conversationRepo.findById(conversationId);
        if (conversationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Conversation non trouvée.");
        }

        var conversation = conversationOpt.get();

        // Sauvegarder le message utilisateur
        Message msgUser = new Message();
        msgUser.setConversation(conversation);
        msgUser.setSender("user");
        msgUser.setContent(userMessage);
        messageRepo.save(msgUser);

        String prompt = customPrompt.isBlank()
                ? cvreadingService.readInternalFileAsString("prompts/promptChatbot.txt")
                : customPrompt;

        List<AbstractMessage> messages = List.of(
                new SystemMessage("<start_of_turn>" + prompt + "<end_of_turn>"),
                new UserMessage("<start_of_turn>" + userMessage + "<end_of_turn>")
        );

        String aiResponse = chatIAService.sendMessageToIA(messages);

        // Sauvegarder la réponse de l’IA
        Message msgAI = new Message();
        msgAI.setConversation(conversation);
        msgAI.setSender("ai");
        msgAI.setContent(aiResponse);
        messageRepo.save(msgAI);

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

        List<Cv> cvs = cvService.findByConversationId(id);
        List<Map<String, Object>> files = cvs.stream().map(cv -> {
            Map<String, Object> fileData = new HashMap<>();
            fileData.put("id", cv.getId()); // 💡
            fileData.put("filename", cv.getOriginalFilename());
            fileData.put("url", "/cv/download/" + cv.getId());
            return fileData;
        }).toList();


        Map<String, Object> response = new HashMap<>();
        response.put("messages", messages);
        response.put("cvs", files); // ✅ envoie les fichiers

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/conversations/{id}")
    @Transactional
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id) {
        Conversation conversation = conversationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée."));

        // ⚠️ Gérer explicitement les dépendances
        List<Cv> cvs = cvService.findByConversationId(id);

        for (Cv cv : cvs) {
            // Retire les références dans firstQuestions
            cv.getFirstQuestions().forEach(fq -> fq.getCvs().remove(cv));
            cv.getFirstQuestions().clear();
        }

        // Suppression explicite des CV
        cvService.deleteAll(cvs);

        // Suppression conversation
        conversationRepo.delete(conversation);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadCv(@PathVariable Long id) throws IOException {
        Cv cv = cvService.findById(id);
        if (cv == null) {
            return ResponseEntity.badRequest().build();
        }

        Path path = Paths.get(cv.getStoragePath());
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource file = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + cv.getOriginalFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(file);
    }





}
