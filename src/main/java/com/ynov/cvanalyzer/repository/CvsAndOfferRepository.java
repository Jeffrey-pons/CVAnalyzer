package com.ynov.cvanalyzer.repository;

import com.ynov.cvanalyzer.entity.CvsAndOffer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvsAndOfferRepository extends CrudRepository<CvsAndOffer,Long> {
}
