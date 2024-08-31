package cyou.devify.blog.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import cyou.devify.blog.entities.Stack;

public interface StackRepository extends JpaRepository<Stack, UUID> {

  Stack findByName(String name);
}
