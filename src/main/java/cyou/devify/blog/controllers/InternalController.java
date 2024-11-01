package cyou.devify.blog.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.repositories.StackRepository;
import cyou.devify.blog.services.ArticleService;
import cyou.devify.blog.services.StackService;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/internal")
@Controller
public class InternalController {
  @Autowired
  UserService userService;
  @Autowired
  StackRepository stackRepository;
  @Autowired
  StackService stackService;
  @Autowired
  ArticleRepository articleRepository;
  @Autowired
  ArticleService articleService;

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
    mv.addObject("username", user.getUsername());
    return mv;
  }

  @GetMapping("/stacks")
  public ModelAndView stacks(ModelAndView mv) {
    mv.setViewName("stack-list");
    mv.addObject("pageTitle", "Lista de Stacks");
    var stacks = stackService.getUnlocked();

    mv.addObject("stacks", stacks);
    return mv;
  }

  @GetMapping("/articles")
  public ModelAndView listUserArticles(ModelAndView mv) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();

    mv.setViewName("user-articles");
    mv.addObject("pageTitle", "Seus artigos");

    var articles = articleService.findAllByUser(user);

    mv.addObject("articles", articles);
    return mv;
  }

  @GetMapping("/article")
  public ModelAndView writeArticle(ModelAndView mv) {
    mv.setViewName("write-article");
    mv.addObject("pageTitle", "Escreva seu novo artigo");

    var stacks = stackService.getUnlocked();
    mv.addObject("stacks", stacks);

    mv.addObject("newStackNextURL", "/internal/article");

    return mv;
  }

  @GetMapping("/article/{articleId}/edit")
  public ModelAndView editArticle(ModelAndView mv, @PathVariable String articleId) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    var article = articleRepository.findById(UUID.fromString(articleId));

    if (article.isEmpty()) {
      mv.addObject("pageTitle", "Artigo não encontrado");
      mv.setViewName("404");
      mv.setStatus(HttpStatus.NOT_FOUND);
      return mv;
    }

    if (!user.getId().equals(article.get().getCreatedBy())) {
      mv.addObject("pageTitle", "Você não tem permissão para editar este artigo");
      mv.setViewName("401");
      mv.setStatus(HttpStatus.UNAUTHORIZED);
      return mv;
    }

    mv.addObject("article", article.get());
    mv.addObject("pageTitle", "Edição do artigo");
    mv.setViewName("article-edit");

    return mv;
  }

  @GetMapping("/article/{articleId}/edit/metadata")
  public ModelAndView editArticleMetadata(ModelAndView mv, @PathVariable String articleId) {
    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    var article = articleRepository.findById(UUID.fromString(articleId));

    if (article.isEmpty()) {
      mv.addObject("pageTitle", "Artigo não encontrado");
      mv.setViewName("404");
      mv.setStatus(HttpStatus.NOT_FOUND);
      return mv;
    }

    if (!user.getId().equals(article.get().getCreatedBy())) {
      mv.addObject("pageTitle", "Você não tem permissão para editar este artigo");
      mv.setViewName("401");
      mv.setStatus(HttpStatus.UNAUTHORIZED);
      return mv;
    }

    mv.addObject("newStackNextURL", String.format("/internal/article/%s/edit/metadata", articleId));
    mv.addObject("article", article.get());
    mv.addObject("pageTitle", "Edição dos metadados do artigo");
    mv.setViewName("article-edit-metadata");

    var stacks = stackService.getUnlocked();
    mv.addObject("stacks", stacks);
    return mv;
  }

}
