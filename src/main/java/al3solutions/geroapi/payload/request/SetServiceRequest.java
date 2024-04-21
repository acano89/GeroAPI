package al3solutions.geroapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SetServiceRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String date;

    private String breakfast;

    private String lunch;

    private String snack;

    private int diaperNum;

    private String shower;

    private int urination;

    private int deposition;
}
