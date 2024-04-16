package al3solutions.geroapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Builder
public class SetServiceRequest {

    @NotBlank
    private String name;

    @NotBlank
    private Date date;

    private String breakfast;

    private String lunch;

    private String snack;

    @NotBlank
    private int diaperNum;

    private String shower;

    @NotBlank
    private int urination;

    @NotBlank
    private int deposition;
}
