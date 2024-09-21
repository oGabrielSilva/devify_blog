package cyou.devify.blog.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import cyou.devify.blog.entities.NewArticleNotificationSubscription;

public interface NewArticleNotificationSubscriptionRepository
    extends JpaRepository<NewArticleNotificationSubscription, UUID> {

  NewArticleNotificationSubscription findByEmail(String email);
}
