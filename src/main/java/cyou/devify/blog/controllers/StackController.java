package cyou.devify.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.repositories.StackRepository;
import cyou.devify.blog.utils.DateFormatter;

@Controller
public class StackController {
  @Autowired
  StackRepository stackRepository;
  @Autowired
  ArticleRepository articleRepository;

  @ModelAttribute
  public DateFormatter dateFormatter() {
    return new DateFormatter();
  }

  @GetMapping("/item/{stackSlug}")
  public ModelAndView publicView(ModelAndView mv, @PathVariable String stackSlug) {
    var stack = stackRepository.findBySlug(stackSlug);

    if (stack == null) {
      mv.setViewName("404");
      mv.setStatus(HttpStatus.NOT_FOUND);
      mv.addObject("pageTitle", "Stack não encontrada");
      return mv;
    }

    mv.setViewName("stack-page");
    mv.addObject("pageTitle", stack.getName());
    mv.addObject("stack", stack);
    mv.addObject("pageDescription", stack.getMetaDescription());

    var articles = articleRepository.findAllByStackAndIsPublishedTrueAndEnabledTrue(stack);
    mv.addObject("articles", articles);
    return mv;
  }
}
