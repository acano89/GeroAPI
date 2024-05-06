package al3solutions.geroapi.payload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SetServiceRequest {


    private String name;

    private String date;

    private String breakfast;

    private String lunch;

    private String snack;

    private int diaperNum;

    private String shower;

    private int urination;

    private int deposition;
}
