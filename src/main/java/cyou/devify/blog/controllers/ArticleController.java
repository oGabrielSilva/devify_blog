package cyou.devify.blog.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.entities.Article;
import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.repositories.StackRepository;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.StringUtils;
import cyou.devify.blog.vm.ArticleViewModel;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ArticleController {
  @Autowired
  StackRepository stackRepository;
  @Autowired
  UserService userService;
  @Autowired
  ArticleRepository articleRepository;

  @PostMapping("/internal/article")
  public ModelAndView create(ModelAndView mv, ArticleViewModel payload, HttpServletRequest request,
      @RequestHeader(value = "User-Agent") String userAgent) {
    mv.setViewName("redirect:/internal/article");

    if (payload == null) {
      mv.addObject("error", "Payload insuficiente");
      return mv;
    }

    if (StringUtils.isNullOrBlank(payload.title())) {
      mv.addObject("error", "Título inválido");
      return mv;
    }

    if (StringUtils.isNullOrBlank(payload.stack())) {
      mv.addObject("error", "Stack não informada");
      return mv;
    }

    var stack = stackRepository.findById(UUID.fromString(payload.stack()));
    if (stack.isEmpty()) {
      mv.addObject("error", "Stack informada não existe");
      return mv;
    }

    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    if (user.isCommon()) {
      mv.addObject("error", "Usuário não tem permissão para manipular artigos");
      return mv;
    }

    var slug = StringUtils.slugify(payload.title());
    var article = new Article(payload.title(), slug, stack.get(), user.getId(), user.getId());
    article = articleRepository.save(article);

    mv.setViewName(String.format("redirect:/internal/article/%s/edit", article.getId().toString()));
    return mv;
  }
}