package a.ynov.back.repository;

import a.ynov.back.entity.ResponseIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseRepository extends JpaRepository<ResponseIA, Long> {
}
