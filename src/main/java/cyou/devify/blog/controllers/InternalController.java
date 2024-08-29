package cyou.devify.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/internal")
@Controller
public class InternalController {
  @Autowired
  UserService userService;

  @ModelAttribute("currentURL")
  public String getCurrentURL(HttpServletRequest request) {
    return request.getRequestURI();
  }

  @GetMapping("/profile")
  public ModelAndView profile(ModelAndView mv, @RequestParam(required = false) String tab) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    mv.setViewName("profile");
    mv.addObject("pageTitle", "Perfil de " + user.getName());
    mv.addObject("tab", StringUtils.isNonNullOrBlank(tab) ? tab.toLowerCase() : "profile");
    mv.addObject("user", user);
    return mv;
  }
}
