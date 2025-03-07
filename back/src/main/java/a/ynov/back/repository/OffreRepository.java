package a.ynov.back.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import a.ynov.back.entity.Offre;

@Repository
public interface OffreRepository extends CrudRepository<Offre, Long> {


}
