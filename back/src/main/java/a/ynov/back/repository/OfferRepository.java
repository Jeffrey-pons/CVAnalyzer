package a.ynov.back.repository;

<<<<<<<< HEAD:back/src/main/java/a/ynov/back/repository/OffreRepository.java
========
import com.ynov.cvanalyzer.entity.Offer;
>>>>>>>> refacto1:back/src/main/java/a/ynov/back/repository/OfferRepository.java
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import a.ynov.back.entity.Offre;

@Repository
public interface OfferRepository extends CrudRepository<Offer, Long> {


}
