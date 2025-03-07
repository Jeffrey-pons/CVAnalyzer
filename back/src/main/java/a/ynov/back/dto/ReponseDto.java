package a.ynov.back.dto;


import a.ynov.back.entity.CvsAndOffer;

public record ReponseDto(String message, CvsAndOffer firstQuestion) {
}
