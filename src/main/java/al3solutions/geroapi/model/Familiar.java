package al3solutions.geroapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Familiar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String name;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "familiars_user",
            joinColumns = @JoinColumn(name = "familiar_User_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> familiarsUserId = new HashSet<>();


    @Size(max = 50)
    @Email
    private String familiarMail;


    @Size(max = 20)
    private String state;


    @Size(max = 20)
    private String reason;


    @Size(max = 20)
    private String place;


    @Size(max = 20)
    private String dayTrip;


    @Size(max = 20)
    private String shower;


    @Size(max = 20)
    private String pickup;

}
