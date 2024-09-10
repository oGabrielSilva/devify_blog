package cyou.devify.blog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cyou.devify.blog.entities.User;
import cyou.devify.blog.enums.Role;
import cyou.devify.blog.exceptions.Forbidden;
import cyou.devify.blog.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
  @Autowired
  UserRepository repository;
  @Value("${devify.owner.username}")
  String ownerUsername;

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

  public boolean isCommon() {
    var user = getCurrentAuthenticatedUser();
    return user != null && user.isCommon();
  }

  public boolean isEditor() {
    var user = getCurrentAuthenticatedUser();
    return user != null && user.isEditor();
  }

  public boolean isMod() {
    var user = getCurrentAuthenticatedUser();
    return user != null && user.isMod();
  }

  public boolean isAdmin() {
    var user = getCurrentAuthenticatedUser();
    return user != null && user.isAdmin();
  }

  public List<User> getEditors() {
    return repository.findByAuthority(Role.EDITOR);
  }

  public List<User> getStaff() {
    return repository.findByAuthorityNot(Role.COMMON);
  }

  public UserRepository getRepository() {
    return repository;
  }

  public User getOwner() {
    return repository.findByUsername(ownerUsername);
  }

}
