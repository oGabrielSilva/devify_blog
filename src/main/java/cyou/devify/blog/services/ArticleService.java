package cyou.devify.blog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cyou.devify.blog.entities.Article;
import cyou.devify.blog.entities.User;
import cyou.devify.blog.repositories.ArticleRepository;
import jakarta.transaction.Transactional;

@Service
public class ArticleService {
  @Autowired
  ArticleRepository articleRepository;

  @Transactional
  public List<Article> findAllByUser(User user) {
    return articleRepository.findAllByCreatedBy(user.getId());
  }
}
