package com.ynov.cvanalyzer.repository;

import com.ynov.cvanalyzer.entity.Cv;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvRepository  extends CrudRepository<Cv, Long> {
}
