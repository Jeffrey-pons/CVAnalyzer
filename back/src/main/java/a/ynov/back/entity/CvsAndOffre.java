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
public class CvsAndOffre {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "offre_id")
    private Offre offre;
    @ManyToMany
    @JoinTable(
            name = "first_question_cv",
            joinColumns = @JoinColumn(name = "first_question_id"),
            inverseJoinColumns = @JoinColumn(name = "cv_id")
    )
    private List<Cv> cvs;

    @OneToOne(mappedBy = "firstQuestion", cascade = CascadeType.ALL)
    private ReponseIA reponseIA;

}
