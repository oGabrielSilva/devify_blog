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
import cyou.devify.blog.vm.SocialViewModel;

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
}
