package cyou.devify.blog.entities;

import java.time.Instant;
import java.util.UUID;

public record MinimizedArticle(
    String title,
    String slug,
    boolean enabled,
    Instant updatedAt,
    UUID createdBy,
    Stack stack) {
}
