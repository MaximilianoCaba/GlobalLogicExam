package microservice.user.validator;

import microservice.user.dto.SignUpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

public class SignUpDTOValidator implements Validator {

  @Autowired
  private MessageSource messageSource;

  @Override
  public boolean supports(Class<?> clazz) {
    return SignUpDTO.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    SignUpDTO user = (SignUpDTO) target;
    String passwordErrorKey = isValidPasswordFormat(user.getPassword());
    if (passwordErrorKey != null) {
      Locale currentLocale = LocaleContextHolder.getLocale();
      String passwordError = messageSource.getMessage(passwordErrorKey, null, currentLocale);
      errors.rejectValue("password", passwordError);
    }
  }

  /**
   * Regular Expression with validate password
   * RULES:
   * The size should be between 8 and 12.
   * Must have only a capital letter.
   * Must have two numbers in any position.
   *
   * @param password
   * @return
   */
  public static String isValidPasswordFormat(String password) {
    if (password.length() < 6 || password.length() > 12) {
      return "error.password.size";
    }

    long countOfUpper = password.chars().filter(Character::isUpperCase).count();
    long countOfDigits = password.chars().filter(Character::isDigit).count();


    if (countOfUpper != 1) {
      return "error.password.upperLetter";
    }

    if (countOfDigits != 2) {
      return "error.password.number";
    }

    return null;
  }
}
