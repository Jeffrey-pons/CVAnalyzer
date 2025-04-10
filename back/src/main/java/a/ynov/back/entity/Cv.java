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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contenu;

    private String originalFilename; // ✅ nom du fichier

    private String storagePath; // ✅ chemin sur le disque (facultatif)

    @ManyToMany(mappedBy = "cvs")
    private List<CvsAndOffer> firstQuestions;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
}
