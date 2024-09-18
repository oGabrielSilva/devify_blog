package cyou.devify.blog.entities;

import java.time.Instant;
import java.util.UUID;

public record MinimizedArticleForResearch(
    String title,
    String slug,
    boolean enabled,
    boolean isPublished,
    UUID publishedBy,
    Instant publishedAt,
    Instant updatedAt) {

}
