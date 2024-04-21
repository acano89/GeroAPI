package al3solutions.geroapi.payload.response;

import al3solutions.geroapi.model.ERole;
import al3solutions.geroapi.model.Role;
import al3solutions.geroapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsersListResponse {

    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public UsersListResponse(Long id, String username, String email, Set<Role>roles){
        this.id=id;
        this.username=username;
        this.email=email;
        List<String> list = new ArrayList<>();
        for (Role role : roles) {
            ERole name = role.getName();
            list.add(String.valueOf(name));
        }
        this.roles= list;
    }
}
