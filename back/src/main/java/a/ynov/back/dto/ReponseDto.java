package a.ynov.back.dto;


import a.ynov.back.entity.CvsAndOffre;

public record ReponseDto(String message, CvsAndOffre firstQuestion) {
}
