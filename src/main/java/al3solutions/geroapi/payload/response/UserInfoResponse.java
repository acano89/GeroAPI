package al3solutions.geroapi.payload.response;

import al3solutions.geroapi.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoResponse {

    Long id;
    private String username;
    private String email;
    private List<String> roles;

    public UserInfoResponse(String username, String email, Set<Role> roles) {
    }
}
