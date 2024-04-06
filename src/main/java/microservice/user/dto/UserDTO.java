package microservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

  private String id;
  private LocalDateTime created;
  private LocalDateTime lastLogin;
  private String token;
  private boolean isActive;
  private String name;
  private String email;
  private String password;
  private List<PhoneDTO> phones;

}
