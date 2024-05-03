package al3solutions.geroapi.payload.request;

import al3solutions.geroapi.model.Familiar;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Builder
public class GetServiceRequest {

    private Date date;

    private String name;

}
