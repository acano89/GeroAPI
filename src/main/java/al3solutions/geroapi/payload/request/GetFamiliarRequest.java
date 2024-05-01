package al3solutions.geroapi.payload.request;

import al3solutions.geroapi.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetFamiliarRequest {

    private String name;
    private Long familiarsUserId;
}
