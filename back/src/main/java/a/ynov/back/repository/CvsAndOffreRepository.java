package a.ynov.back.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import a.ynov.back.entity.CvsAndOffre;

@Repository
public interface CvsAndOffreRepository extends CrudRepository<CvsAndOffre, Long> {
}
