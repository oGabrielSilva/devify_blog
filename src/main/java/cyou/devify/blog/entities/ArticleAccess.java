package cyou.devify.blog.entities;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleAccess {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private UUID articleId;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant accessedAt;

  public ArticleAccess(Article article) {
    this.articleId = article.getId();
  }
}
