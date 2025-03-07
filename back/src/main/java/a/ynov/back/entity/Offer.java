package a.ynov.back.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class Offer {
    @Id
    @GeneratedValue
    private int id;
    @Lob
    private String description;
}
