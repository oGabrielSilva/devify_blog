package cyou.devify.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.repositories.UserRepository;
import cyou.devify.blog.services.TokenService;
import cyou.devify.blog.services.UploadImageService;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.AuthValidation;
import cyou.devify.blog.utils.StringUtils;
import cyou.devify.blog.vm.ChangePasswordViewModel;
import cyou.devify.blog.vm.ProfileFormViewModel;
import cyou.devify.blog.vm.SessionViewModel;
import cyou.devify.blog.vm.SocialViewModel;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/internal/profile")
@Controller
public class ProfileController {
  @Autowired
  UserService userService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  UploadImageService imageService;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  TokenService tokenService;

  @PostMapping
  public String updateProfile(ProfileFormViewModel payload) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    boolean hasChanges = false;

    if (StringUtils.isNonNullOrBlank(payload.name()) && !payload.name().equals(user.getName())) {
      user.setName(payload.name());
      hasChanges = true;
    }

    if (StringUtils.isNonNullOrBlank(payload.pseudonym()) && !payload.pseudonym().equals(user.getPseudonym())) {
      user.setPseudonym(payload.pseudonym());
      hasChanges = true;
    }

    if (StringUtils.isNonNullOrBlank(payload.bio()) && !payload.bio().equals(user.getBio())) {
      user.setBio(payload.bio());
      hasChanges = true;
    }

    if (payload.avatar() != null && !payload.avatar().isEmpty()) {
      try {
        var result = imageService.saveAvatar(payload.avatar(), user);
        if (result != null) {
          user.setAvatarURL(result.URI());
          user.setAvatarFilePath(result.filePath());
          hasChanges = true;
        }
      } catch (Exception e) {
        System.out.println("\n" + e.getMessage() + "\n");
      }
    }

    if (hasChanges) {
      userRepository.save(user);
    }
    return "redirect:/internal/profile";
  }

  @PostMapping("/social")
  public String updateSocial(SocialViewModel payload) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    var social = user.getSocial();
    boolean hasChanges = false;

    if (StringUtils.isNonNullOrBlank(payload.instagram()) && !payload.instagram().equals(social.getInstagram())) {
      social.setInstagram(payload.instagram());
      hasChanges = true;
    }

    if (StringUtils.isNonNullOrBlank(payload.discord()) && !payload.discord().equals(social.getDiscord())) {
      social.setDiscord(payload.discord());
      hasChanges = true;
    }

    if (StringUtils.isNonNullOrBlank(payload.github()) && !payload.github().equals(social.getGithub())) {
      social.setGithub(payload.github());
      hasChanges = true;
    }

    if (StringUtils.isNonNullOrBlank(payload.linkedin()) && !payload.linkedin().equals(social.getLinkedin())) {
      social.setLinkedin(payload.linkedin());
      hasChanges = true;
    }

    if (StringUtils.isNonNullOrBlank(payload.site()) && !payload.site().equals(social.getSite())) {
      social.setSite(payload.site());
      hasChanges = true;
    }

    if (StringUtils.isNonNullOrBlank(payload.twitter()) && !payload.twitter().equals(social.getTwitter())) {
      social.setTwitter(payload.twitter());
      hasChanges = true;
    }

    if (StringUtils.isNonNullOrBlank(payload.youtube()) && !payload.youtube().equals(social.getYoutube())) {
      social.setYoutube(payload.youtube());
      hasChanges = true;
    }

    if (hasChanges) {
      user.setSocial(social);
      userRepository.save(user);
    }

    return "redirect:/internal/profile?tab=social";
  }

  @PostMapping("/email")
  public ModelAndView updateEmail(SessionViewModel payload, ModelAndView mv, HttpServletResponse response) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    mv.setViewName("redirect:/internal/profile?tab=security");

    AuthValidation validation = new AuthValidation();
    if (!validation.isEmailValid(payload.email())) {
      mv.addObject("error", "Informe um endereço de email válido");
      return mv;
    }

    if (StringUtils.isNonNullOrBlank(payload.email())) {
      if (payload.email().equals(user.getEmail())) {
        mv.addObject("error", "Por favor, insira um email diferente do seu atual");
        return mv;
      }
      var userByEmail = userRepository.findByEmail(payload.email());

      if (userByEmail != null) {
        mv.addObject("error", String.format("Usuário com o email %s já está cadastrado", payload.email()));
        return mv;
      }

      if (!passwordEncoder.matches(payload.password(), user.getPassword())) {
        mv.addObject("error", "Credenciais inválidas");
        return mv;
      }

      user.setEmail(payload.email());
      user = userRepository.save(user);

      response.addCookie(tokenService.createCookie(tokenService.create(user)));
    }

    return mv;
  }

  @PostMapping("/password")
  public ModelAndView updatePassword(ChangePasswordViewModel payload, ModelAndView mv, HttpServletResponse response) {
    mv.setViewName("redirect:/internal/profile?tab=security");

    AuthValidation validation = new AuthValidation();
    if (!validation.isPasswordValid(payload.currentPassword()) || !validation.isPasswordValid(payload.newPassword())
        || !validation.isPasswordValid(payload.confirmPassword())) {
      mv.addObject("error", "As senhas precisam ser válidas");
      return mv;
    }

    if (!payload.newPassword().equals(payload.confirmPassword())) {
      mv.addObject("error", "Senhas não batem");
      return mv;
    }

    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    if (!passwordEncoder.matches(payload.currentPassword(), user.getPassword())) {
      mv.addObject("error", "Credenciais inválidas");
      return mv;
    }

    user.setPassword(passwordEncoder.encode(payload.newPassword()));
    user = userRepository.save(user);

    return mv;
  }
}
