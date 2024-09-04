package cyou.devify.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import cyou.devify.blog.entities.ArticleAccess;

public interface ArticleAccessRepository extends JpaRepository<ArticleAccess, Long> {

}
