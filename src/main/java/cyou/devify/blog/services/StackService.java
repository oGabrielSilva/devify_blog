package cyou.devify.blog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cyou.devify.blog.entities.Stack;
import cyou.devify.blog.repositories.StackRepository;

@Service
public class StackService {
  @Autowired
  StackRepository stackRepository;

  public List<Stack> getUnlocked() {
    var stacks = stackRepository.findAll();
    stacks.removeIf(stack -> stack.isLocked());

    return stacks;
  }
}
