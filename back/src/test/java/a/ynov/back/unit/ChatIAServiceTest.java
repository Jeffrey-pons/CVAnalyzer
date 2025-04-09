package a.ynov.back.unit;

import a.ynov.back.service.ChatIAService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ChatIAServiceTest {

    @Mock
    private OllamaChatModel chatModel;

    private ChatIAService chatIAService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatIAService = new ChatIAService(chatModel);
    }

    @Test
    void sendMessageToIA_validResponse() {
        String inputMessage = "Hello IA!";

        AbstractMessage abstractMessage = mock(AbstractMessage.class);
        when(abstractMessage.getText()).thenReturn(inputMessage);

        String expectedResponse = "Bonjour, comment puis-je vous aider ?";

        Generation generation = mock(Generation.class);
        AssistantMessage assistantMessage = mock(AssistantMessage.class);
        when(generation.getOutput()).thenReturn(assistantMessage);
        when(assistantMessage.getText()).thenReturn(expectedResponse);

        ChatResponse chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(generation);

        when(chatModel.stream(any(Prompt.class))).thenReturn(Flux.just(chatResponse));

        String result = chatIAService.sendMessageToIA(List.of(abstractMessage));

        verify(chatModel, times(1)).stream(any(Prompt.class));

        assertEquals(expectedResponse, result);
    }

    @Test
    void sendMessageToIA_noResponse() {
        String inputMessage = "Hello IA!";
        AbstractMessage abstractMessage = mock(AbstractMessage.class);
        when(abstractMessage.getText()).thenReturn(inputMessage);

        when(chatModel.stream(any(Prompt.class))).thenReturn(Flux.empty());

        String result = chatIAService.sendMessageToIA(List.of(abstractMessage));
        verify(chatModel, times(1)).stream(any(Prompt.class));
        assertEquals("", result);
    }

    @Test
    void sendMessageToIA_emptyResponse() {
        String inputMessage = "Hello IA!";
        AbstractMessage abstractMessage = mock(AbstractMessage.class);
        when(abstractMessage.getText()).thenReturn(inputMessage);

        Generation generation = mock(Generation.class);
        when(generation.toString()).thenReturn("");

        ChatResponse chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(generation);

        when(chatModel.stream(any(Prompt.class))).thenReturn(Flux.just(chatResponse));

        String result = chatIAService.sendMessageToIA(List.of(abstractMessage));
        verify(chatModel, times(1)).stream(any(Prompt.class));
        assertEquals("", result);
    }

    @Test
    void sendMessageToIA_exception() {
        String inputMessage = "Hello IA!";
        AbstractMessage abstractMessage = mock(AbstractMessage.class);
        when(abstractMessage.getText()).thenReturn(inputMessage);

        when(chatModel.stream(any(Prompt.class))).thenThrow(new RuntimeException("Chat model error"));

        String result = chatIAService.sendMessageToIA(List.of(abstractMessage));
        verify(chatModel, times(1)).stream(any(Prompt.class));
        assertEquals("", result);
    }
}
