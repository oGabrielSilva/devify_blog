package cyou.devify.blog.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.entities.User;
import cyou.devify.blog.repositories.UserRepository;
import cyou.devify.blog.services.TokenService;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.AuthValidation;
import cyou.devify.blog.utils.StringUtils;
import cyou.devify.blog.vm.SessionViewModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/session")
public class SessionController {
  @Autowired
  UserService userService;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  TokenService tokenService;
  @Autowired
  UserRepository userRepository;

  @GetMapping
  public ModelAndView session(@RequestParam(required = false) String next) {
    return generateGenericMV(next);
  }

  @PostMapping
  public ModelAndView sessionIn(SessionViewModel payload, HttpServletResponse response) {
    var validation = new AuthValidation();
    var mv = generateGenericMV(payload.next());

    if (!validation.isEmailValid(payload.email())) {
      mv.addObject("error", "Email não é válido");
      return mv;
    }

    if (!validation.isPasswordValid(payload.password())) {
      mv.addObject("error", "Senha não é válida");
      return mv;
    }

    var userByEmail = userService.loadUserByUsername(payload.email());
    if (userByEmail == null) {
      mv.addObject("error", "Usuário não encontrado");
      return mv;
    }

    if (!passwordEncoder.matches(payload.password(), userByEmail.getPassword())) {
      mv.addObject("error", "Credenciais inválidas");
      return mv;
    }
    var token = tokenService.create((User) userByEmail);
    response.addCookie(tokenService.createCookie(token));

    mv.clear();
    mv.setViewName("redirect:" + (StringUtils.isNonNullOrBlank(payload.next()) ? payload.next() : "/"));
    return mv;
  }

  @PostMapping("/sign-up")
  public ModelAndView signUp(SessionViewModel payload, HttpServletResponse response) {
    var validation = new AuthValidation();
    var mv = generateGenericMV(payload.next());

    if (!validation.isEmailValid(payload.email())) {
      mv.addObject("error", "Email não é válido");
      return mv;
    }

    if (!validation.isPasswordValid(payload.password())) {
      mv.addObject("error", "Senha não é válida");
      return mv;
    }

    var userByEmail = userService.loadUserByUsername(payload.email());
    if (userByEmail != null) {
      mv.addObject("error", "Usuário já cadastrado");
      return mv;
    }

    var user = userRepository
        .save(new User(payload.email().split("@")[0], payload.email(), "/images/avatar_placeholder.png",
            passwordEncoder.encode(payload.password())));
    var token = tokenService.create(user);
    response.addCookie(tokenService.createCookie(token));

    mv.clear();
    mv.setViewName("redirect:" + (StringUtils.isNonNullOrBlank(payload.next()) ? payload.next() : "/"));
    return mv;
  }

  @PostMapping("/out")
  public String signOut(HttpServletResponse response) {
    response.addCookie(tokenService.createInvalidAuthorizationCookie());
    return "redirect:/";
  }

  @ResponseBody
  @GetMapping(path = "/is-logged-in", produces = { "application/json" })
  public Map<String, Boolean> isLoggedIn(HttpServletRequest request) {
    Map<String, Boolean> map = new HashMap<>();
    map.put("isLoggedIn", false);

    var token = tokenService.recoveryToken(request);

    if (token == null) {
      return map;
    }

    var email = tokenService.requireSubject(token);
    if (email == null) {
      return map;
    }

    var user = userRepository.existsByEmail(email);

    map.replace("isLoggedIn", user);
    return map;
  }

  private ModelAndView generateGenericMV(String next) {
    var mv = new ModelAndView();
    mv.addObject("noIndexPage", true);
    mv.addObject("nextURL", next);
    mv.addObject("pageTitle", "Faça login ou crie sua conta");
    mv.setViewName("session");
    return mv;
  }
}
