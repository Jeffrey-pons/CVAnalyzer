package com.ynov.cvanalyzer.dto;

import com.ynov.cvanalyzer.entity.Cv;
import com.ynov.cvanalyzer.entity.Offre;

public record CvsAndOffreDto(Offre offre, Iterable<Cv> cvs) {
}
