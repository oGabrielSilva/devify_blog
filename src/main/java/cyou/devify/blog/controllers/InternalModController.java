package cyou.devify.blog.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.services.ArticleService;
import cyou.devify.blog.services.UserService;

@Controller
@RequestMapping("/internal/mod")
public class InternalModController {
  @Autowired
  ArticleRepository articleRepository;
  @Autowired
  ArticleService articleService;

  @Autowired
  UserService userService;

  @GetMapping("/articles")
  public ModelAndView allArticles(ModelAndView mv) {
    var articles = articleRepository.findAll();
    mv.addObject("pageTitle", "Moderação - Todos os artigos");
    mv.addObject("articles", articles);

    mv.setViewName("mod-articles");

    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    mv.addObject("moder", user);
    return mv;
  }

  @GetMapping("/article/{articleId}/state")
  public ModelAndView allArticles(ModelAndView mv, @PathVariable String articleId) {
    var article = articleRepository.findById(UUID.fromString(articleId));

    if (article.isEmpty()) {
      mv.addObject("pageTitle", "Artigo não encontrado");
      mv.setViewName("404");
      mv.setStatus(HttpStatus.NOT_FOUND);
      return mv;
    }

    mv.addObject("pageTitle", String.format("Moderação - Mudar estado do artigo %s", article.get().getTitle()));
    mv.addObject("article", article.get());
    var creator = articleService.getCreator(article.get());
    mv.addObject("creator", creator);

    mv.setViewName("mod-article-state");

    return mv;
  }
}
