package com.ynov.cvanalyzer.dto;

import com.ynov.cvanalyzer.entity.Cv;
import com.ynov.cvanalyzer.entity.Offer;

public record CvsAndOfferDto(Offer offre, Iterable<Cv> cvs) {
}
