package microservice.user.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import microservice.user.dto.UserDTO;
import microservice.user.exeption.SignUpException;
import microservice.user.exeption.response.GenericErrorResponse;
import microservice.user.service.SecurityService;
import microservice.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static microservice.user.utils.ErrorUtils.buildError;

@Component
@RequiredArgsConstructor
public class JwtConfig extends OncePerRequestFilter {

  private final UserService userService;

  private final SecurityService securityService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain
  ) throws ServletException, IOException {

    try {
      final String authorizationHeader = request.getHeader("Authorization");

      String email = null;

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        String jwt = authorizationHeader.substring(7);
        email = securityService.validateJwtTokenAndGetEmail(jwt);
      }

      SecurityContext securityContext = SecurityContextHolder.getContext();

      if (email != null && securityContext != null && securityContext.getAuthentication() == null) {
        UserDTO userDTO = userService.getUserByEmail(email);

        if (userDTO != null) {
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDTO, null, null);
          usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
      }
      chain.doFilter(request, response);
    } catch (SignUpException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      GenericErrorResponse genericErrorResponse = buildError(e.getMessage(), HttpStatus.UNAUTHORIZED);
      new ObjectMapper().writeValue(response.getOutputStream(), genericErrorResponse);
    }
  }
}