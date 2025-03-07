package com.ynov.cvanalyzer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
public class ResponseIA {
    @Id
    @GeneratedValue
    private int id;
    @Lob
    private String message;
    @OneToOne
    @JoinColumn(name = "first_question_id", unique = true)
    private CvsAndOffer firstQuestion;

    @OneToOne
    @JoinColumn(name = "question_id", unique = true)
    private Question question;

}
