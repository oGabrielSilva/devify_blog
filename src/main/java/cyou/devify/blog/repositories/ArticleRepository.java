package cyou.devify.blog.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cyou.devify.blog.entities.Article;
import cyou.devify.blog.entities.MinimizedArticle;
import cyou.devify.blog.entities.MinimizedArticleForResearch;
import cyou.devify.blog.entities.Stack;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
  Article findBySlug(String slug);

  boolean existsBySlug(String slug);

  List<Article> findAllByCreatedBy(UUID createdBy);

  List<MinimizedArticle> findAllByStackAndIsPublishedTrueAndEnabledTrue(Stack stack);

  List<MinimizedArticle> findAllMinimizedByCreatedByAndIsPublishedTrueAndEnabledTrue(UUID createdBy);

  List<MinimizedArticle> findAllMinimizedByIsPublishedTrueAndEnabledTrue();

  Integer countByCreatedByAndEnabledTrueAndIsPublishedTrue(UUID createdBy);

  List<Article> findByCreatedByAndEnabledTrueAndIsPublishedTrueOrderByCreatedAtDesc(UUID createdBy);

  @Query("SELECT new cyou.devify.blog.entities.MinimizedArticleForResearch(a.title, a.slug, a.description, a.enabled, a.isPublished, a.createdBy, a.publishedBy, a.publishedAt, a.updatedAt, a.stack) "
      +
      "FROM Article a " +
      "WHERE (LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "OR LOWER(CAST(a.content AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "OR LOWER(a.metaDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "OR LOWER(a.stack.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
      "AND a.enabled = true " +
      "AND a.isPublished = true")
  List<MinimizedArticleForResearch> searchByKeyword(@Param("keyword") String keyword);

}
