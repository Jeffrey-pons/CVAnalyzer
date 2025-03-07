package com.ynov.cvanalyzer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
public class Question {
    @Id
    @GeneratedValue
    private int id;
    private String question;
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private ResponseIA responseIA;
}
