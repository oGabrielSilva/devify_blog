package cyou.devify.blog.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.services.StackService;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.DateFormatter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Controller
public class HomeController {

  @Autowired
  UserService userService;
  @Autowired
  ArticleRepository articleRepository;
  @Autowired
  StackService stackService;

  @ModelAttribute
  public DateFormatter dateFormatter() {
    return new DateFormatter();
  }

  @GetMapping("/")
  public ModelAndView ping(ModelAndView mv) {
    mv.setViewName("index");
    mv.addObject("pageTitle", "Início");

    var staff = userService.getStaff();
    var stacks = stackService.getUnlocked();
    var articles = articleRepository.findAllMinimizedByIsPublishedTrueAndEnabledTrue();

    var articlesResult = articles.stream()
        .map(a -> Map.of("article", a, "editor",
            staff.stream().filter(user -> user.getId().equals(a.createdBy())).findFirst().get()))
        .collect(Collectors.toList());

    mv.addObject("articles", articlesResult);
    mv.addObject("staff", staff);
    mv.addObject("stacks", stacks);

    return mv;
  }

  @Transactional
  @GetMapping("/search")
  public ModelAndView search(ModelAndView mv, @RequestParam(required = false) String query) {
    if (query == null) {
      mv.setViewName("redirect:/");
      return mv;
    }

    mv.addObject("query", query);
    mv.setViewName("search");

    var articles = articleRepository.searchByKeyword(query);

    var resultCount = articles.size();

    if (resultCount < 1) {
      mv.addObject("pageTitle", "Nenhum resultado encontrado para a sua pesquisa");
    } else {
      mv.addObject("pageTitle", String.format("%s %s para a sua pesquisa", String.valueOf(resultCount),
          resultCount > 1 ? "resultados encontrados" : "resultado encontrado"));
    }

    var editorsId = articles.stream().map(a -> a.createdBy()).collect(Collectors.toList());
    var editors = userService.getRepository().findAllMinimizedUserByIdIn(editorsId);

    var result = articles.stream()
        .map(a -> Map.of("article", a, "editor",
            editors.stream().filter(e -> e.id().equals(a.createdBy())).findFirst().get()))
        .collect(Collectors.toList());

    mv.addObject("result", result);
    return mv;
  }

  @GetMapping("/about")
  public ModelAndView about(ModelAndView mv) {
    mv.setViewName("about");
    mv.addObject("pageTitle", "Quem somos?");
    var owner = userService.getOwner();

    mv.addObject("owner", owner);
    mv.addObject("pageDescription",
        "Devify, o que é? Quem criou e por quê? Entenda um pouco da nossa história e da nossa motivação");
    return mv;
  }

  @GetMapping("/contact")
  public ModelAndView contact(ModelAndView mv) {
    mv.setViewName("contact");
    mv.addObject("pageTitle", "Entrar em contato");

    mv.addObject("staff", userService.getStaffIfEnabled());
    mv.addObject("pageDescription",
        "Conheça a equipe por trás da Devify e entre em contato para trocar ideias ou discutir oportunidades");
    return mv;
  }

  @GetMapping("/privacy-policy")
  public ModelAndView privacyPolicy(ModelAndView mv) {
    mv.setViewName("privacy-policy");
    mv.addObject("pageTitle", "Política Privacidade");

    return mv;
  }

  @GetMapping("/error")
  public ModelAndView handleError(HttpServletRequest request) {
    var mv = new ModelAndView();
    mv.setViewName("error");
    mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null) {
      Integer statusCode = Integer.valueOf(status.toString());

      if (statusCode == HttpStatus.FORBIDDEN.value()) {
        mv.setStatus(HttpStatus.FORBIDDEN);
        mv.setViewName("403");
        return mv;
      } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
        mv.setStatus(HttpStatus.NOT_FOUND);
        mv.setViewName("404");
        return mv;
      }
    }
    return mv;
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
