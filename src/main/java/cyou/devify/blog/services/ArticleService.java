package cyou.devify.blog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cyou.devify.blog.entities.Article;
import cyou.devify.blog.entities.User;
import cyou.devify.blog.repositories.ArticleRepository;
import cyou.devify.blog.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class ArticleService {
  @Autowired
  ArticleRepository articleRepository;
  @Autowired
  UserRepository userRepository;

  @Transactional
  public Article findBySlug(String slug) {
    return articleRepository.findBySlug(slug);
  }

  @Transactional
  public List<Article> findAllByUser(User user) {
    return articleRepository.findAllByCreatedBy(user.getId());
  }

  public User getCreator(Article article) {
    return userRepository.findById(article.getCreatedBy()).get();
  }
}
