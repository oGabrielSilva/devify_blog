package cyou.devify.blog.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import cyou.devify.blog.entities.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenService {
  public static final String COOKIE_AUTHORIZATION_KEY = "__devify";
  private final String issuer;
  private final String audience;
  private Algorithm algorithm;

  public TokenService(@Value("${devify.jwt.secret}") String tokenSecret,
      @Value("${devify.jwt.issuer}") String issuer,
      @Value("${devify.jwt.audience}") String audience) {
    this.algorithm = Algorithm.HMAC256(tokenSecret);
    this.issuer = issuer;
    this.audience = audience;
  }

  public String create(User user) {
    try {
      return JWT.create().withIssuer(issuer).withAudience(audience).withSubject(user.getEmail())
          .withClaim("authorities", user.allRolesAsStrings())
          .withExpiresAt(LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.UTC)).sign(algorithm);
    } catch (JWTCreationException ex) {
      return "";
    }
  }

  public String requireSubject(String token) {
    try {
      return decode(token).getSubject();
    } catch (Exception e) {
      return null;
    }
  }

  public DecodedJWT decode(String token) {
    try {
      return JWT.require(algorithm).withIssuer(issuer).build().verify(token);
    } catch (Exception e) {
      return null;
    }
  }

  public String recoveryToken(HttpServletRequest req) {
    String authorization = req.getHeader("Authorization");
    if (authorization == null) {
      var cookies = req.getCookies();
      if (cookies == null || cookies.length < 1)
        return authorization;
      var op = Stream.of(cookies).filter(ck -> {
        return ck.getName().equals(COOKIE_AUTHORIZATION_KEY);
      }).findFirst();
      if (op.isEmpty())
        return authorization;
      authorization = op.get().getValue();
    }
    return authorization.contains(" ") ? authorization.split(" ")[1] : authorization;
  }

  public Cookie createCookie(String token) {
    var c = new Cookie(COOKIE_AUTHORIZATION_KEY, token);
    c.setHttpOnly(true);
    c.setAttribute("SameSite", "Strict");
    c.setPath("/");
    c.setMaxAge(60 * 60 * 24 * 7);
    return c;
  }

  public Cookie createInvalidAuthorizationCookie() {
    var c = new Cookie(COOKIE_AUTHORIZATION_KEY, "");
    c.setHttpOnly(true);
    c.setAttribute("SameSite", "Strict");
    c.setPath("/");
    c.setMaxAge(60);
    return c;
  }

}
