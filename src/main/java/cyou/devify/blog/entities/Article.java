package cyou.devify.blog.entities;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String title;

  @Column(unique = true)
  private String slug;

  @Lob
  @Column(columnDefinition = "TEXT")
  @Basic(fetch = FetchType.LAZY)
  private String content = "<p>Hello world</p>";

  @Column(columnDefinition = "TEXT")
  @Basic(fetch = FetchType.LAZY)
  private String description = "";

  @Column(length = 225)
  private String metaDescription = "";

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stack_id", nullable = false)
  private Stack stack;

  private boolean enabled = true;
  private boolean isPublished = false;
  private UUID publishedBy = null;
  private Instant publishedAt = null;
  private UUID unpublishedBy = null;
  private Instant unpublishedAt = null;
  private UUID createdBy;
  private UUID updatedBy;

  private long accessCounter = 0;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;

  public Article(String title, String slug, Stack stack, UUID createdBy, UUID updatedBy) {
    this.title = title;
    this.slug = slug;
    this.stack = stack;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
  }
}
