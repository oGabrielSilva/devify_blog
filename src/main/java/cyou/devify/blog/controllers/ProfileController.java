package cyou.devify.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cyou.devify.blog.repositories.UserRepository;
import cyou.devify.blog.services.UploadImageService;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.StringUtils;
import cyou.devify.blog.vm.ProfileFormViewModel;

@RequestMapping("/internal/profile")
@Controller
public class ProfileController {
  @Autowired
  UserService userService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  UploadImageService imageService;

  @PostMapping
  public String updateProfile(ProfileFormViewModel payload) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    System.out.println("User -> " + user.getEmail());
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
}
