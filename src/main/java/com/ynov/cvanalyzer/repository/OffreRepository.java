package com.ynov.cvanalyzer.repository;

import com.ynov.cvanalyzer.entity.Offre;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffreRepository extends CrudRepository<Offre, Long> {


}
