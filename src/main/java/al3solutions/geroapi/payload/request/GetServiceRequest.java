package al3solutions.geroapi.payload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetServiceRequest {

    private String date;

    private String name;

}
