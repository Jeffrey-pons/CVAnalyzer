package com.ynov.cvanalyzer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Clob;

@Entity
@Data
@ToString
public class Offre {
    @Id
    @GeneratedValue
    private int id;
    @Lob
    private String description;
}
