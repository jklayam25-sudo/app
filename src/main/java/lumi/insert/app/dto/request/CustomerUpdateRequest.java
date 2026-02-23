package lumi.insert.app.dto.request; 
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
public class CustomerUpdateRequest extends CustomerCreateRequest{
    
    Boolean isActive;

    Double Latitude;

    Double Longitude;

}
