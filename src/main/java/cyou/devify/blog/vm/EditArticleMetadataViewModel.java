package cyou.devify.blog.vm;

import java.util.UUID;

public record EditArticleMetadataViewModel(String title, String description, String metaDescription, UUID stack) {

}
