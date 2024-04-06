package microservice.user.exeption.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericErrorResponse {
  private List<GenericError> errors = new ArrayList<>();
}
