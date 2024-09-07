package cyou.devify.blog.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import cyou.devify.blog.entities.User;
import cyou.devify.blog.enums.Role;

public interface UserRepository extends JpaRepository<User, UUID> {
  User findByEmail(String email);

  User findByUsername(String username);

  boolean existsByEmail(String email);

  List<User> findByAuthority(Role authority);

  List<User> findByAuthorityNot(Role authority);
}
