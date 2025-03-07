package a.ynov.back.dto;


import a.ynov.back.entity.Cv;
import a.ynov.back.entity.Offre;

public record CvsAndOffreDto(Offre offre, Iterable<Cv> cvs) {
}
