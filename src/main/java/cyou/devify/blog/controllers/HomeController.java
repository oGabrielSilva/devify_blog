package cyou.devify.blog.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class HomeController {
  @GetMapping
  public ModelAndView ping(ModelAndView mv) {
    mv.setViewName("index");
    mv.addObject("pageTitle", "Início");
    return mv;
  }
}
