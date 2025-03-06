package com.ynov.cvanalyzer.dto;

import com.ynov.cvanalyzer.entity.CvsAndOffre;

public record ReponseDto(String message, CvsAndOffre firstQuestion) {
}
