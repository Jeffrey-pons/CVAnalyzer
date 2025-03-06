package com.ynov.cvanalyzer.repository;

import com.ynov.cvanalyzer.entity.ReponseIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseRepository  extends JpaRepository<ReponseIA, Long> {
}
