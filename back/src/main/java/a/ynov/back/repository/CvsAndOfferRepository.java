package a.ynov.back.repository;


<<<<<<<< HEAD:back/src/main/java/a/ynov/back/repository/CvsAndOffreRepository.java
========
import com.ynov.cvanalyzer.entity.CvsAndOffer;
>>>>>>>> refacto1:back/src/main/java/a/ynov/back/repository/CvsAndOfferRepository.java
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import a.ynov.back.entity.CvsAndOffre;

@Repository
<<<<<<<< HEAD:back/src/main/java/a/ynov/back/repository/CvsAndOffreRepository.java
public interface CvsAndOffreRepository extends CrudRepository<CvsAndOffre, Long> {
========
public interface CvsAndOfferRepository extends CrudRepository<CvsAndOffer,Long> {
>>>>>>>> refacto1:back/src/main/java/a/ynov/back/repository/CvsAndOfferRepository.java
}
