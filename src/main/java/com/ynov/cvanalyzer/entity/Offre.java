package com.ynov.cvanalyzer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class Offre {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String description;
}
