package microservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDTO {

  @NotEmpty(message = "number is required.")
  private int number;

  @NotEmpty(message = "cityCode is required.")
  private int cityCode;

  @NotEmpty(message = "countryCode is required.")
  private String countryCode;

}
