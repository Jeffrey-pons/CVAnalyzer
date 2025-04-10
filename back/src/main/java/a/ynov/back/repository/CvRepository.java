package a.ynov.back.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import a.ynov.back.entity.Cv;

import java.util.List;

@Repository
public interface CvRepository extends CrudRepository<Cv, Long> {

    List<Cv> findByConversationId(Long conversationId);
}
