package cyou.devify.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.services.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

  @Autowired
  UserService userService;

  @GetMapping("/")
  public ModelAndView ping(ModelAndView mv) {
    mv.setViewName("index");
    mv.addObject("pageTitle", "Início");

    return mv;
  }

  @GetMapping("/about")
  public ModelAndView about(ModelAndView mv) {
    mv.setViewName("about");
    mv.addObject("pageTitle", "Quem somos?");
    var owner = userService.getOwner();

    mv.addObject("owner", owner);
    return mv;
  }

  @GetMapping("/contact")
  public ModelAndView contact(ModelAndView mv) {
    mv.setViewName("contact");
    mv.addObject("pageTitle", "Entrar em contato");

    mv.addObject("staff", userService.getStaff());

    return mv;
  }

  @GetMapping("/privacy-policy")
  public ModelAndView privacyPolicy(ModelAndView mv) {
    mv.setViewName("privacy-policy");
    mv.addObject("pageTitle", "Política Privacidade");

    return mv;
  }

  @GetMapping("/error")
  public String handleError(HttpServletRequest request) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null) {
      Integer statusCode = Integer.valueOf(status.toString());

      if (statusCode == HttpStatus.FORBIDDEN.value()) {
        return "403";
      } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
        return "404";
      } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        return "500";
      }
    }
    return "error";
  }

  @GetMapping("/403")
  public ModelAndView forbidden(ModelAndView mv) {
    mv.setViewName("403");

    mv.addObject("pageTitle", "Acesso rejeitado pelo emissor");
    return mv;
  }

  @PostMapping("/403")
  public ModelAndView postForbidden(ModelAndView mv) {
    mv.setViewName("403");

    mv.addObject("pageTitle", "Acesso rejeitado pelo emissor");
    return mv;
  }
}
