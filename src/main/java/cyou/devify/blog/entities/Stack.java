package cyou.devify.blog.entities;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
public class Stack {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(unique = true, length = 25)
  private String name;

  @Column(unique = true)
  private String slug;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 225)
  private String metaDescription;

  private boolean isLocked = false;

  private UUID createdBy;
  private UUID updatedBy;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;

  public Stack(String name, String slug, String description, String metaDescription, UUID createdBy, UUID updatedBy) {
    this.name = name;
    this.slug = slug;
    this.description = description;
    this.metaDescription = metaDescription;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
  }
}
