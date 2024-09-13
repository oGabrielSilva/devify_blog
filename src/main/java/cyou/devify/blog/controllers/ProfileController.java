package cyou.devify.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.repositories.UserRepository;
import cyou.devify.blog.services.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

  @Autowired
  UserService userService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ArticleRepository articleRepository;

  @GetMapping("/{username}")
  public ModelAndView publicProfile(ModelAndView mv, @PathVariable String username) {
    var currentUser = userService.getCurrentAuthenticatedUserOrThrowsForbidden();

    mv.setViewName("profile-public");

    if (currentUser.getUsername().equals(username)) {
      mv.addObject("user", currentUser);
      mv.addObject("pageTitle", String.format("%s - Perfil", currentUser.getName()));

      var count = articleRepository.countByCreatedByAndEnabledTrueAndIsPublishedTrue(currentUser.getId());
      mv.addObject("articleCount", count);

      var articles = articleRepository
          .findByCreatedByAndEnabledTrueAndIsPublishedTrueOrderByCreatedAtDesc(currentUser.getId());

      mv.addObject("articles", articles);
      return mv;
    }

    var user = userRepository.findByUsername(username);

    if (user == null || !user.isEnabled()) {
      mv.setViewName("profile-404");
      mv.addObject("pageTitle", "Perfil não encontrado");
      mv.setStatus(HttpStatus.NOT_FOUND);
      return mv;
    }

    return mv;
  }

}
