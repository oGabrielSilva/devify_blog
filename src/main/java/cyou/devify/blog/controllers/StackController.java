package cyou.devify.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.entities.Stack;
import cyou.devify.blog.repositories.StackRepository;
import cyou.devify.blog.services.UserService;
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

    var user = userService.getCurrentAuthenticatedUserOrThrowsForbidden();
    var stack = new Stack(name, payload.description(), payload.metaDescription(), user.getId(),
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

}
