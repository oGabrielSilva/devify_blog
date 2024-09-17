package cyou.devify.blog.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.entities.Article;
import cyou.devify.blog.exceptions.BadRequest;
import cyou.devify.blog.exceptions.NotFound;
import cyou.devify.blog.exceptions.Unauthorized;
import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.repositories.StackRepository;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.StringUtils;
import cyou.devify.blog.vm.ArticleViewModel;
import cyou.devify.blog.vm.EditArticleMetadataViewModel;
import cyou.devify.blog.vm.EditArticleViewModel;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/internal/article")
public class InternalArticleController {
  @Autowired
  StackRepository stackRepository;
  @Autowired
  UserService userService;
  @Autowired
  ArticleRepository articleRepository;

  @PostMapping
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

  @PostMapping("/{articleId}/save")
  public ModelAndView save(ModelAndView mv, EditArticleViewModel payload, @PathVariable String articleId) {
    if (payload == null) {
      throw new BadRequest("Conteúdo não informado");
    }
    var articleOpt = articleRepository.findById(UUID.fromString(articleId));

    if (articleOpt.isEmpty()) {
      throw new NotFound("Artigo não encontrado");
    }

    var article = articleOpt.get();

    var user = userService.getCurrentAuthenticatedUser();
    if (!user.getId().equals(article.getCreatedBy())) {
      throw new Unauthorized("Usuário não tem permissão para alterar o artigo designado");
    }

    article.setContent(payload.content());
    article = articleRepository.save(article);

    mv.setViewName(String.format("redirect:/internal/article/%s/edit", article.getId()));
    // mv.addObject("success", "Artigo salvo");

    return mv;
  }

  @PostMapping("/{articleId}/save/metadata")
  public ModelAndView saveMetadata(ModelAndView mv, EditArticleMetadataViewModel payload,
      @PathVariable String articleId) {
    if (payload == null) {
      throw new BadRequest("Conteúdo não informado");
    }
    var articleOpt = articleRepository.findById(UUID.fromString(articleId));

    if (articleOpt.isEmpty()) {
      throw new NotFound("Artigo não encontrado");
    }

    var article = articleOpt.get();

    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    if (!user.getId().equals(article.getCreatedBy())) {
      throw new Unauthorized("Usuário não tem permissão para alterar o artigo designado");
    }

    if (StringUtils.isNullOrBlank(payload.title())) {
      mv.addObject("error", "Título inválido");
      return mv;
    }
    article.setTitle(payload.title());
    article.setSlug(StringUtils.slugify(article.getTitle()));

    var articleBySlug = articleRepository.findBySlug(article.getSlug());

    if (articleBySlug != null) {
      article.setSlug(article.getSlug() + System.currentTimeMillis());
    }

    if (payload.stack() == null) {
      mv.addObject("error", "Stack não informada");
      return mv;
    }

    var stack = stackRepository.findById(payload.stack());
    if (stack.isEmpty()) {
      mv.addObject("error", "Stack informada não existe");
      return mv;
    }
    article.setStack(stack.get());

    if (StringUtils.isNonNullOrBlank(payload.metaDescription()) && payload.metaDescription().length() <= 225) {
      article.setMetaDescription(payload.metaDescription());
    }

    article.setDescription(payload.description());

    articleRepository.save(article);

    mv.setViewName(String.format("redirect:/internal/article/%s/edit/metadata", article.getId()));
    mv.addObject("success", "Metadados atualizados");

    return mv;
  }
}
