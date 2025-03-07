package a.ynov.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import a.ynov.back.entity.ReponseIA;

@Repository
public interface ReponseRepository extends JpaRepository<ReponseIA, Long> {
}
