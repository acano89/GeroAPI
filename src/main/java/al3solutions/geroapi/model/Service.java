package al3solutions.geroapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "serveis")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    // "yyyy-MM-dd" Format
    private Date date;

    private String breakfast;

    private String lunch;

    private String snack;

    private int diaperNum;

    private String shower;

    private int urination;

    private int deposition;

}
