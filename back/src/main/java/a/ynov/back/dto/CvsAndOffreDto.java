package a.ynov.back.dto;


import a.ynov.back.entity.Cv;
import a.ynov.back.entity.Offer;


public record CvsAndOffreDto(Offer offre, Iterable<Cv> cvs) {
}
