package cyou.devify.blog.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import cyou.devify.blog.entities.Article;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
  Article findBySlug(String slug);

  List<Article> findAllByCreatedBy(UUID createdBy);
}
