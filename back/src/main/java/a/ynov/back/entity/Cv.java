package a.ynov.back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Cv {
    @Id
    @GeneratedValue
    private int id;
    @Lob
    private String contenu;

    @ManyToMany(mappedBy = "cvs")
    private List<CvsAndOffer> firstQuestions;


}
