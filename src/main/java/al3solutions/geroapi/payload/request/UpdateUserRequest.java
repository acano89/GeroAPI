package al3solutions.geroapi.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest{

    private String newName;
    private String newEmail;

}
