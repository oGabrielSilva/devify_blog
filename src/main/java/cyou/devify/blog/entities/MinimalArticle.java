package cyou.devify.blog.entities;

import java.time.Instant;

public record MinimalArticle(
    String title,
    String slug,
    boolean enabled,
    Instant updatedAt,
    Stack stack) {

}
