package microservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {

  @Schema(description = "Personal user name", example = "Charles Mendez")
  private String name;

  @Schema(description = "Personal user email", example = "myEmail@company.com")
  @NotBlank(message = "The email cannot be empty.")
  @NotEmpty(message = "email is required.")
  @Email(message = "Email should be valid.")
  private String email;

  @Schema(description = "The password must be between 6 and 12 characters, must contain 2 numbers and 1 capital letter", example = "Mypass1word2")
  private String password;

  @Schema(description = "List of phones associated")
  private List<PhoneDTO> phones = new ArrayList<>();

}
