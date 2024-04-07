package microservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User description")
public class UserDTO {

  @Schema(description = "User id", example = "KJkuhga-JB835bn-JBsjabfh4-kjndnfl4")
  private String id;

  @Schema(description = "Time when user was created", example = "2024-04-07T00:37:43.1648727")
  private LocalDateTime created;

  @Schema(description = "Time when user was logged", example = "2024-04-07T00:37:43.1648727")
  private LocalDateTime lastLogin;

  @Schema(description = "token authentication", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c3VhcmlvMTNAM2V4YW1wbGUuY29tIiwiaWF0IjoxNzEyNDYxMDYzLCJleHAiOjE3MTI0NjExNDl9.a95_x-mIjXmDwx1H1TzMGagCLY4w-mQz0ejc8GJea08")
  private String token;

  @Schema(description = "User is active", example = "true")
  private boolean isActive;

  @Schema(description = "Name of person", example = "Charles Mendez")
  private String name;

  @Schema(description = "Personal email", example = "myEmail@company.com")
  private String email;

  @Schema(description = "Private password encrypted", example = "LJKHNsvbafonkl598jnasfbbjf63246")
  private String password;

  @Schema(description = "List of phones associated")
  private List<PhoneDTO> phones;

}
