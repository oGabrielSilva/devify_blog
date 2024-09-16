package cyou.devify.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.entities.User;
import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.repositories.StackRepository;
import cyou.devify.blog.services.ArticleService;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.DateFormatter;

@Controller
public class ArticleController {
  @Autowired
  StackRepository stackRepository;
  @Autowired
  ArticleService articleService;
  @Autowired
  ArticleRepository articleRepository;
  @Autowired
  UserService userService;

  @ModelAttribute
  public DateFormatter dateFormatter() {
    return new DateFormatter();
  }

  @GetMapping("/item/{stackSlug}/{slug}")
  public ModelAndView article(ModelAndView mv, @PathVariable String stackSlug, @PathVariable String slug) {
    var article = articleService.findBySlug(slug);

    if (article == null || !article.getStack().getSlug().equals(stackSlug)) {
      mv.setViewName("404");
      mv.setStatus(HttpStatus.NOT_FOUND);
      mv.addObject("pageTitle", "Artigo não encontrado");
      return mv;
    }

    var stack = article.getStack();

    mv.setViewName("article-page");
    mv.addObject("article", article);
    mv.addObject("stack", stack);
    mv.addObject("pageTitle", article.getTitle());

    User creator;
    User currentUser = userService.getCurrentAuthenticatedUserOrThrowsForbidden();

    if (currentUser != null && article.getCreatedBy().equals(currentUser.getId())) {
      creator = currentUser;
    } else {
      creator = userService.getRepository().findById(article.getCreatedBy()).get();
    }

    if (creator == null) {
      mv.setViewName("404");
      mv.setStatus(HttpStatus.NOT_FOUND);
      mv.addObject("pageTitle", "Artigo não encontrado");
      return mv;
    }

    var editorArticles = articleRepository.findAllMinimalByCreatedByAndIsPublishedTrue(creator.getId());
    editorArticles.removeIf(art -> art.slug().equals(article.getSlug()));

    mv.addObject("editorArticles", editorArticles);
    mv.addObject("creator", creator);
    return mv;
  }

}
