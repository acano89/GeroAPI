package al3solutions.geroapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @NotBlank
    @CreationTimestamp
    private String date;

    private String breakfast;

    private String lunch;

    private String snack;

    @NotBlank
    private int diaperNum;

    private String shower;

    @NotBlank
    private int urination;

    @NotBlank
    private int deposition;

}
