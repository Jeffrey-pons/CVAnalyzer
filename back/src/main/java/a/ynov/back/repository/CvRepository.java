package a.ynov.back.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import a.ynov.back.entity.Cv;

@Repository
public interface CvRepository extends CrudRepository<Cv, Long> {
}
