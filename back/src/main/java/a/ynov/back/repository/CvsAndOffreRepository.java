package a.ynov.back.repository;


import a.ynov.back.entity.CvsAndOffer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvsAndOffreRepository extends CrudRepository<CvsAndOffer, Long> {
}