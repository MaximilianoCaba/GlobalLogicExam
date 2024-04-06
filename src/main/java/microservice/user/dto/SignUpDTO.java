package microservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {

  private String name;

  @NotEmpty(message = "email is required.")
  @Email(message = "Email should be valid.")
  private String email;

  private String password;
  private List<PhoneDTO> phones = new ArrayList<>();

}
