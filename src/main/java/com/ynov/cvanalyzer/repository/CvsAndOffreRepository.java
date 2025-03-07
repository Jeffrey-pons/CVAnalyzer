package com.ynov.cvanalyzer.repository;

import com.ynov.cvanalyzer.entity.CvsAndOffre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvsAndOffreRepository extends CrudRepository<CvsAndOffre,Long> {
}
