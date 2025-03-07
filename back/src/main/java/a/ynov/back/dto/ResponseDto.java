package a.ynov.back.dto;


import a.ynov.back.entity.CvsAndOffer;

public record ResponseDto(String message, CvsAndOffer firstQuestion) {
}
