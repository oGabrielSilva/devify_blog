package cyou.devify.blog.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import cyou.devify.blog.entities.Article;
import cyou.devify.blog.entities.MinimalArticle;
import cyou.devify.blog.entities.Stack;
import jakarta.transaction.Transactional;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
  Article findBySlug(String slug);

  List<Article> findAllByCreatedBy(UUID createdBy);

  List<MinimalArticle> findAllByStackAndIsPublishedTrueAndEnabledTrue(Stack stack);

  List<MinimalArticle> findAllMinimalByCreatedByAndIsPublishedTrueAndEnabledTrue(UUID createdBy);

  Integer countByCreatedByAndEnabledTrueAndIsPublishedTrue(UUID createdBy);

  @Transactional
  List<Article> findByCreatedByAndEnabledTrueAndIsPublishedTrueOrderByCreatedAtDesc(UUID createdBy);
}
