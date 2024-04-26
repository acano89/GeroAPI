package al3solutions.geroapi.payload.request;

import al3solutions.geroapi.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SetFamiliarRequest {

    @NotBlank
    private String name;

    private User familiarUser;

    private String familiarMail;

    private String state;

    private String reason;


    private String place;

    private String dayTrip;

    private String shower;

    private String pickup;


}
