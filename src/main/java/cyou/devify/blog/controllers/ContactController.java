package cyou.devify.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.services.UserService;

@Controller
public class ContactController {
  @Autowired
  UserService userService;

  @GetMapping("/contact")
  public ModelAndView contact(ModelAndView mv) {
    mv.setViewName("contact");
    mv.addObject("pageTitle", "Entrar em contato");

    mv.addObject("staff", userService.getStaff());

    return mv;
  }

}
