package cyou.devify.blog.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.repositories.UserRepository;
import cyou.devify.blog.services.AmazonS3Service;
import cyou.devify.blog.services.TokenService;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.AuthValidation;
import cyou.devify.blog.utils.StringUtils;
import cyou.devify.blog.vm.ChangePasswordViewModel;
import cyou.devify.blog.vm.ProfileFormViewModel;
import cyou.devify.blog.vm.SessionViewModel;
import cyou.devify.blog.vm.SocialViewModel;
import cyou.devify.blog.vm.UpdateUsernameViewModel;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/internal/profile")
@Controller
public class InternalProfileController {
  @Autowired
  UserService userService;
  @Autowired
  UserRepository userRepository;
  // @Autowired
  // UploadImageService imageService;
  @Autowired
  AmazonS3Service blobService;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  TokenService tokenService;

  @Value("${props.app.domain}")
  String appDomain;

  @PostMapping
  public String updateProfile(ProfileFormViewModel payload) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    boolean hasChanges = false;

    if (StringUtils.isNonNullOrBlank(payload.name()) && !payload.name().equals(user.getName())) {
      user.setName(payload.name());
      hasChanges = true;
    }

    if (StringUtils.isNullOrBlank(payload.pseudonym()) && StringUtils.isNonNullOrBlank(user.getPseudonym())) {
      user.setPseudonym(payload.pseudonym());
      hasChanges = true;
    } else if (StringUtils.isNonNullOrBlank(payload.pseudonym()) && !payload.pseudonym().equals(user.getPseudonym())) {
      user.setPseudonym(payload.pseudonym());
      hasChanges = true;
    }

    if (StringUtils.isNonNullOrBlank(payload.bio()) && !payload.bio().equals(user.getBio())) {
      String bio = payload.bio();

      if (bio.length() > 500) {
        bio = bio.substring(0, 500);
      }
      user.setBio(bio);
      hasChanges = true;
    }

    if (payload.avatar() != null && !payload.avatar().isEmpty()) {
      try {
        // var result = imageService.saveAvatar(payload.avatar(), user);
        // if (result != null) {
        // user.setAvatarURL(result.URI());
        // user.setAvatarFilePath(result.filePath());
        // hasChanges = true;
        // }
        Map<String, String> metadata = new HashMap<>();
        metadata.put("timestamp", String.valueOf(System.currentTimeMillis()));
        metadata.put("generated-by", appDomain);

        var result = blobService.uploadMultipart(payload.avatar(), user.getId() + ".webp", "avatar", metadata);
        if (result != null) {
          user.setAvatarURL(result.getUri());
          user.setAvatarFilePath(result.getOrigin());
          hasChanges = true;
        }
      } catch (Exception e) {
        System.out.println("\n" + e.getMessage() + "\n");
      }
    }

    if (hasChanges) {
      userRepository.save(user);
      return "redirect:/internal/profile?success=Perfil+atualizado";
    }
    return "redirect:/internal/profile";
  }

  @PostMapping("/social")
  public String updateSocial(SocialViewModel payload) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    var social = user.getSocial();

    social.setInstagram(payload.instagram());
    social.setDiscord(payload.discord());
    social.setGithub(payload.github());
    social.setLinkedin(payload.linkedin());
    social.setSite(payload.site());
    social.setTwitter(payload.twitter());
    social.setYoutube(payload.youtube());
    social.setBluesky(payload.bluesky());
    social.setFacebook(payload.facebook());
    social.setMastodon(payload.mastodon());
    social.setReddit(payload.reddit());
    social.setThreads(payload.threads());
    social.setTiktok(payload.tiktok());

    userRepository.save(user);

    return "redirect:/internal/profile?tab=social&info=Social+atualizado";
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

      mv.addObject("success", String.format("Email atualizado para %s", user.getEmail()));

      response.addCookie(tokenService.createCookie(tokenService.create(user)));
    }

    return mv;
  }

  @PostMapping("/username")
  public ModelAndView updateUsername(UpdateUsernameViewModel payload, ModelAndView mv) {
    mv.setViewName("redirect:/internal/profile?tab=security");

    var username = payload.username();
    System.out.println(username);

    if (StringUtils.isNonNullOrBlank(username)) {
      var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();

      if (username.equals(user.getUsername())) {
        mv.addObject("error", "Por favor, insira um username diferente do seu atual");
        return mv;
      }
      var userByUsername = userRepository.findByUsername(username);

      if (userByUsername != null) {
        mv.addObject("error", "Username informado já está em uso");
        return mv;
      }

      user.setUsername(username);
      userRepository.save(user);

      mv.addObject("success", String.format("Nome de usuário atualizado para @%s", user.getUsername()));
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

    mv.addObject("success", "Senha atualizada");
    return mv;
  }
}
