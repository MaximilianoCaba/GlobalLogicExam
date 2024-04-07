package microservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDTO {

  @Schema(description = "phone number", example = "115437313")
  @NotEmpty(message = "number is required.")
  private int number;

  @Schema(description = "City code", example = "12")
  @NotEmpty(message = "cityCode is required.")
  private int cityCode;

  @Schema(description = "Country code", example = "AR")
  @NotEmpty(message = "countryCode is required.")
  private String countryCode;

}
