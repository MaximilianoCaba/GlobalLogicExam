package microservice.user.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import microservice.user.exeption.SignUpException;
import microservice.user.service.SecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class SecurityServiceImpl implements SecurityService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long expiration;

  @PostConstruct
  private void init() {
    if (secret == null || secret.isEmpty()) {
      throw new IllegalStateException("The secret JWT is null or empty");
    }
  }

  @Override
  public String generateToken(String string) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);
    return Jwts.builder()
        .setSubject(string)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  @Override
  public String validateJwtTokenAndGetEmail(String token) throws SignUpException {
    try {
      var claims = Jwts
          .parser()
          .setSigningKey(secret)
          .parseClaimsJws(token)
          .getBody();
      if (claims.getExpiration().before(new Date())) {
        throw new SignUpException("token expired");
      }
      return claims.getSubject();
    } catch (SignUpException e) {
      throw e;
    } catch (Exception e) {
      throw new SignUpException("invalid token");
    }
  }


}
