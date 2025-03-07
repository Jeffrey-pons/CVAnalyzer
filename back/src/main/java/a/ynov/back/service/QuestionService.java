package a.ynov.back.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import a.ynov.back.repository.QuestionRepository;

@Service
@Getter
@Setter
public class QuestionService {
    private QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
}
