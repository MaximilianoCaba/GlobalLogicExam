package microservice.user.exeption.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericError {
  private Timestamp timestamp;
  private int codigo;
  private String detail;
}
