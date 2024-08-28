package cyou.devify.blog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cyou.devify.blog.entities.User;
import cyou.devify.blog.exceptions.Forbidden;
import cyou.devify.blog.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
  @Autowired
  UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByEmail(username);
  }

  public User getCurrentAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getPrincipal() instanceof User ? (User) authentication.getPrincipal() : null;
  }

  public User getCurrentAuthenticatedUserOrThrowsForbidden() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication.getPrincipal() instanceof User))
      throw new Forbidden();
    return (User) authentication.getPrincipal();
  }

  public void userIsAuthenticatedOrElseThrowsForbidden() {
    if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User))
      throw new Forbidden();
  }

  public boolean isAuthenticated() {
    return getCurrentAuthenticatedUser() != null;
  }

}
