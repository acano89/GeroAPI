package al3solutions.geroapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoResponse {

    Long id;
    private String username;
    private String email;
    private List<String> roles;
}
