package a.ynov.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import a.ynov.back.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
