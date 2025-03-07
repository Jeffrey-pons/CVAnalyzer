package com.ynov.cvanalyzer.repository;

import com.ynov.cvanalyzer.entity.ResponseIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<ResponseIA, Long> {
}
