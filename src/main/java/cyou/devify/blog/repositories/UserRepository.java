package cyou.devify.blog.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import cyou.devify.blog.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
  User findByEmail(String email);

  boolean existsByEmail(String email);
}
