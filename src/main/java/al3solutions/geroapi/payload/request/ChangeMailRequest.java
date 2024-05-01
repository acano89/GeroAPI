package al3solutions.geroapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ChangeMailRequest {

   @NotBlank
    private String username;

   @NotBlank
    private String newEmail;

}
