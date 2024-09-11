package cyou.devify.blog.controllers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.entities.User;
import cyou.devify.blog.enums.Role;
import cyou.devify.blog.repositories.UserRepository;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.AuthValidation;
import cyou.devify.blog.utils.StringUtils;
import cyou.devify.blog.vm.AdminCreateUserViewModel;
import cyou.devify.blog.vm.ChangeUserStateViewModel;
import cyou.devify.blog.vm.UpdateAuthorityViewModel;

@Controller
@RequestMapping("/internal/admin")
public class InternalAdminController {
  @Autowired
  UserService userService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

  @GetMapping("/users")
  public ModelAndView users(ModelAndView mv) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    var users = userRepository
        .findAll()
        .stream()
        .filter(userPredicate -> !userPredicate.getId().equals(user.getId()))
        .collect(Collectors.toList());

    mv.addObject("pageTitle", "Administração - Usuários");
    mv.addObject("users", users);

    var authorities = List.of(Role.values())
        .stream()
        .filter(role -> role != Role.ADMIN && role != Role.ROOT)
        .collect(Collectors.toList());

    if (user.isRoot()) {
      authorities.add(Role.ADMIN);
    }

    mv.addObject("authorities", authorities);

    mv.setViewName("admin-users");
    return mv;
  }

  @GetMapping("/user")
  public ModelAndView newUserView(ModelAndView mv) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();

    mv.addObject("pageTitle", "Administração - Cadastre usuários");
    var authorities = List.of(Role.values())
        .stream()
        .filter(role -> role != Role.ADMIN && role != Role.ROOT)
        .collect(Collectors.toList());

    mv.addObject("authorities", authorities);

    if (user.isRoot()) {
      authorities.add(Role.ADMIN);
    }

    mv.setViewName("admin-new-user");
    return mv;
  }

  @PostMapping("/user")
  public ModelAndView newUser(ModelAndView mv, AdminCreateUserViewModel payload) {
    mv.setViewName("redirect:/internal/admin/user");

    var validation = new AuthValidation();

    if (!validation.isEmailValid(payload.email())) {
      mv.addObject("error", "Email informado não foi considerado válido");
      return mv;
    }

    if (StringUtils.isNullOrBlank(payload.username()) || payload.username().length() > 30) {
      mv.addObject("error", "Username informado não foi considerado válido");
      return mv;
    }

    var userByUsername = userRepository.findByUsername(payload.username());
    if (userByUsername != null) {
      mv.addObject("error", "Username já utilizado por outro usuário");
      return mv;
    }

    var userByEmail = userRepository.findByEmail(payload.email());
    if (userByEmail != null) {
      mv.addObject("error", "Email já utilizado por outro usuário");
      return mv;
    }

    if (StringUtils.isNullOrBlank(payload.password()) || payload.password().length() < 8) {
      mv.addObject("error", "Senha nula ou muito curta (pelo menos 8 caracteres)");
      return mv;
    }

    if (!(payload.authority() instanceof Role)) {
      mv.addObject("error", "Autoridade não definida");
      return mv;
    }

    var newUser = new User();

    newUser.setAuthority(payload.authority());
    newUser.setName(payload.name());
    newUser.setEmail(payload.email());
    newUser.setUsername(StringUtils.slugify(payload.username()));
    newUser.setPassword(passwordEncoder.encode(payload.password()));

    if (StringUtils.isNullOrBlank(payload.name())) {
      newUser.setName(payload.email().split("@")[0]);
    }

    newUser = userRepository.save(newUser);
    mv.addObject("success", String.format("Usuário @%s cadastrado", newUser.getUsername()));
    return mv;
  }

  @PostMapping("/user/{userId}/authority")
  public ModelAndView authority(ModelAndView mv, @PathVariable String userId, UpdateAuthorityViewModel payload) {
    var userOpt = userRepository.findById(UUID.fromString(userId));
    mv.setViewName("redirect:/internal/admin/users");

    if (userOpt.isEmpty()) {
      mv.addObject("error", "Usuário não encontrado");
      return mv;
    }

    if (!(payload.authority() instanceof Role)) {
      mv.addObject("error", "Autoridade não informada ou inválida");
      return mv;
    }

    var user = userOpt.get();

    if (!user.getAuthority().equals(payload.authority())) {
      user.setAuthority(payload.authority());
      userRepository.save(user);
    }

    return mv;
  }

  @PostMapping("/user/{userId}/state")
  public ModelAndView changeUserState(ModelAndView mv, @PathVariable String userId, ChangeUserStateViewModel payload) {
    var userOpt = userRepository.findById(UUID.fromString(userId));
    mv.setViewName("redirect:/internal/admin/users");

    if (userOpt.isEmpty()) {
      mv.addObject("error", "Usuário não encontrado");
      return mv;
    }

    var user = userOpt.get();

    if (user.isEnabled()) {
      var reason = payload.reason();

      if (StringUtils.isNullOrBlank(reason)) {
        mv.addObject("error", "Informe um motivo para poder desabilitar um perfil");
        return mv;
      }

      user.setDisabledForReason(reason);
      user.setDisabledBy(userService.getCurrentAuthenticatedUserOrThrowsForbidden().getId());
      user.setDisabledAt(Instant.now());
    }

    user.setEnabled(!user.isEnabled());
    userRepository.save(user);

    return mv;
  }

}
