package cyou.devify.blog.controllers;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.repositories.StackRepository;
import cyou.devify.blog.services.ArticleService;
import cyou.devify.blog.services.EmailService;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.DateFormatter;

@Controller
@RequestMapping("/internal/mod")
public class InternalModController {
  @Autowired
  ArticleRepository articleRepository;
  @Autowired
  ArticleService articleService;
  @Autowired
  UserService userService;
  @Autowired
  StackRepository stackRepository;

  @Autowired
  EmailService emailService;

  @ModelAttribute
  public DateFormatter dateFormatter() {
    return new DateFormatter();
  }

  @GetMapping("/stacks/locked")
  public ModelAndView allLockedStacks(ModelAndView mv) {
    var stacks = stackRepository.findByIsLockedTrue();

    mv.addObject("pageTitle", "Moderação - Stacks desativadas");
    // mv.addObject("stacks", stacks);

    if (stacks.size() > 0) {
      var userIds = stacks.stream()
          .map(stack -> stack.getLockedBy())
          .collect(Collectors.toList());

      var users = userService.getRepository()
          .findAllMinimizedUserByIdIn(userIds);

      var stackUserMap = stacks.stream()
          .map(stack -> Map.of(
              "stack", stack,
              "locker", users.stream().filter(user -> user.id().equals(stack.getLockedBy())).findFirst().get()))
          .collect(Collectors.toList());

      mv.addObject("stacksLocked", stackUserMap);
    }

    mv.setViewName("mod-stacks-locked");
    return mv;
  }

  @PostMapping("/stack/{stackId}/unlock")
  public ModelAndView unlockStack(ModelAndView mv, @PathVariable String stackId) {
    mv.setViewName("redirect:/internal/mod/stacks/locked");

    var stackOpt = stackRepository.findById(UUID.fromString(stackId));

    if (stackOpt.isEmpty()) {
      mv.addObject("error", "Stack não encontrada");
      mv.setStatus(HttpStatus.NOT_FOUND);
      return mv;
    }

    var stack = stackOpt.get();
    if (!stack.isLocked()) {
      return mv;
    }

    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();

    stack.setLocked(false);
    stack.setUnlockedAt(Instant.now());
    stack.setUnlockedBy(user.getId());

    stackRepository.save(stack);

    mv.addObject("success", "Stack desbloqueada");
    return mv;
  }

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

  @PostMapping("/article/{articleId}/state")
  public ModelAndView setArticleState(ModelAndView mv, @PathVariable String articleId) {
    var articleOpt = articleRepository.findById(UUID.fromString(articleId));

    if (articleOpt.isEmpty()) {
      mv.addObject("pageTitle", "Artigo não encontrado");
      mv.setViewName("404");
      mv.setStatus(HttpStatus.NOT_FOUND);
      return mv;
    }

    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    var article = articleOpt.get();

    if (article.isPublished()) {
      article.setPublished(false);
      article.setUpdatedBy(user.getId());
      article.setUnpublishedBy(user.getId());
      article.setUnpublishedAt(Instant.now());
    } else {
      article.setPublishedAt(Instant.now());
      article.setPublished(true);
      article.setUpdatedBy(user.getId());
      article.setPublishedBy(user.getId());
    }

    articleRepository.save(article);

    mv.setViewName(String.format("redirect:/internal/mod/article/%s/state", article.getId()));

    if (article.isPublished()) {
      var editor = user.getId().equals(article.getCreatedBy()) ? user
          : userService.getRepository().findById(article.getCreatedBy()).get();

      emailService.sendMessageToAllSubscriptionNewArticle(article, editor != null ? editor : user);

      mv.addObject("success", "Artigo publicado");
    } else {
      mv.addObject("info", "Artigo removido do público");
    }

    return mv;
  }
}
