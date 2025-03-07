package com.ynov.cvanalyzer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
public class Cv {
    @Id
    @GeneratedValue
    private int id;
    @Lob
    private String contenu;
}
