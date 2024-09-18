package cyou.devify.blog.entities;

import java.time.Instant;
import java.util.UUID;

public record MinimizedArticleForResearch(
    String title,
    String slug,
    String description,
    boolean enabled,
    boolean isPublished,
    UUID createdBy,
    UUID publishedBy,
    Instant publishedAt,
    Instant updatedAt,
    Stack stack) {

}
