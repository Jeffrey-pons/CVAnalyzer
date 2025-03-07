package com.ynov.cvanalyzer.dto;

import com.ynov.cvanalyzer.entity.CvsAndOffer;

public record ResponseDto(String message, CvsAndOffer firstQuestion) {
}
