package cyou.devify.blog.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cyou.devify.blog.repositories.UserRepository;
import cyou.devify.blog.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
  @Autowired
  UserRepository userRepository;
  @Autowired
  TokenService tokenService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    var token = tokenService.recoveryToken(request);

    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    var email = tokenService.requireSubject(token);
    if (email == null) {
      filterChain.doFilter(request, response);
      return;
    }

    var user = userRepository.findByEmail(email);
    if (user == null || !user.isEnabled()) {
      filterChain.doFilter(request, response);
      response.addCookie(tokenService.createInvalidAuthorizationCookie());
      return;
    }

    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
    String path = request.getServletPath();
    return path.contains("session") ||
        path.contains("static/images/avatar") ||
        path.contains("site.webmanifest") ||
        path.contains("about.txt") ||
        path.contains("favicon.ico") ||
        path.contains("favicon-16x16.png") ||
        path.contains("favicon-32x32.png") ||
        path.contains("android-chrome-192x192.png") ||
        path.contains("android-chrome-512x512.png") ||
        path.contains("apple-touch-icon.png") ||
        path.contains("/images/") ||
        path.contains("/javascript/") ||
        path.contains("/css/");
  }
}
