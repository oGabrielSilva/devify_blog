package cyou.devify.blog.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  private String userAgent;
  private String ipAddress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Article article;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant createdAt;

  public ArticleAccess(String userAgent, String ipAddress, Article article) {
    this.userAgent = userAgent;
    this.ipAddress = ipAddress;
    this.article = article;
  }
}
