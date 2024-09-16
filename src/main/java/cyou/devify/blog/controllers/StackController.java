package cyou.devify.blog.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.entities.Stack;
import cyou.devify.blog.exceptions.Forbidden;
import cyou.devify.blog.repositories.StackRepository;
import cyou.devify.blog.services.UserService;
import cyou.devify.blog.utils.StringUtils;
import cyou.devify.blog.vm.StackViewModel;

@Controller
public class StackController {
  @Autowired
  StackRepository stackRepository;
  @Autowired
  UserService userService;

  @PostMapping("/internal/stack")
  public ModelAndView create(ModelAndView mv, StackViewModel payload) {
    mv.setViewName("redirect:/internal/stacks");
    if (StringUtils.isNonNullOrBlank(payload.next()) && payload.next().startsWith("/")) {
      mv.setViewName(String.format("redirect:%s", payload.next()));
    }

    String name = payload.name();
    if (name == null || name.trim().length() < 1) {
      mv.addObject("error", "ID da Stack não pode ser vazio");
      return mv;
    }
    name = name.trim();

    var stackByName = stackRepository.findByName(name);

    if (stackByName != null) {
      mv.addObject("error", String.format("Stack %s já está cadastrada", name));
      return mv;
    }

    String stackSlug = StringUtils.slugify(name);

    var stackBySlug = stackRepository.findBySlug(stackSlug);

    if (stackBySlug != null) {
      mv.addObject("error", String.format("Stack %s já está cadastrada", stackSlug));
      return mv;
    }

    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    var stack = new Stack(name, stackSlug, payload.description(), payload.metaDescription(), user.getId(),
        user.getId());

    if (stack.getMetaDescription() != null) {
      String metaDescription = stack.getMetaDescription().trim();

      if (metaDescription.length() > 225) {
        metaDescription = metaDescription.substring(0, 222).trim().concat("...");
      }

      stack.setMetaDescription(metaDescription);
    }

    if (stack.getDescription() != null) {
      stack.setDescription(stack.getDescription().trim());
    }

    stackRepository.save(stack);
    return mv;
  }

  @PostMapping("/internal/stack/{stackId}/edit")
  public ModelAndView edit(ModelAndView mv, StackViewModel payload, @PathVariable String stackId) {
    mv.setViewName("redirect:/internal/stacks");

    if (stackId == null) {
      mv.addObject("error", "Stack não encontrada");
      return mv;
    }

    if (StringUtils.isNullOrBlank(payload.name())) {
      mv.addObject("error", "Nome da Stack não pode ser nulo ou invisível");
      return mv;
    }

    var stackOpt = stackRepository.findById(UUID.fromString(stackId));

    if (stackOpt.isEmpty()) {
      mv.addObject("error", "Stack não encontrada");
      return mv;
    }

    var stack = stackOpt.get();
    if (!stack.getName().equals(payload.name())) {
      var stackByName = stackRepository.findByName(payload.name());

      if (stackByName != null) {
        mv.addObject("error", String.format("Stack %s já está cadastrada. Escolha um nome diferente", payload.name()));
        return mv;
      }

      String slug = StringUtils.slugify(payload.name());
      var stackBySlug = stackRepository.findBySlug(slug);

      if (stackBySlug != null) {
        mv.addObject("error", String.format("Stack %s já está cadastrada. Escolha um nome diferente", slug));
        return mv;
      }
    }

    boolean hasChanges = false;

    if (!stack.getName().equals(payload.name())) {
      stack.setName(payload.name());
      stack.setSlug(StringUtils.slugify(payload.name()));
      hasChanges = true;
    }

    if (!stack.getDescription().equals(payload.description())) {
      stack.setDescription(payload.description());
      hasChanges = true;
    }

    if (!stack.getMetaDescription().equals(payload.metaDescription())) {
      stack.setMetaDescription(payload.metaDescription());
      hasChanges = true;
    }

    if (hasChanges) {
      var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
      stack.setUpdatedBy(user.getId());

      stackRepository.save(stack);
    }

    return mv;
  }

  @PostMapping("/internal/stack/{stackId}/disable")
  public ModelAndView disable(ModelAndView mv, @PathVariable String stackId) {
    mv.setViewName("redirect:/internal/stacks");

    if (stackId == null) {
      mv.addObject("error", "Stack não encontrada");
      return mv;
    }

    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();

    if (!user.isMod()) {
      throw new Forbidden();
    }

    var stackOpt = stackRepository.findById(UUID.fromString(stackId));

    if (stackOpt.isEmpty()) {
      mv.addObject("error", "Stack não encontrada");
      return mv;
    }

    var stack = stackOpt.get();
    stack.setLocked(true);
    stack.setUpdatedBy(user.getId());

    stackRepository.save(stack);

    return mv;
  }

}
