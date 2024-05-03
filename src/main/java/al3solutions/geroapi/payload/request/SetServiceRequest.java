package al3solutions.geroapi.payload.request;

import al3solutions.geroapi.model.Familiar;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Builder
public class SetServiceRequest {


    private String name;

    private Date date;

    private String breakfast;

    private String lunch;

    private String snack;

    private int diaperNum;

    private String shower;

    private int urination;

    private int deposition;
}
