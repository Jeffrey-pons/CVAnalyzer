package com.ynov.cvanalyzer.repository;

import com.ynov.cvanalyzer.entity.Offer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends CrudRepository<Offer, Long> {


}
