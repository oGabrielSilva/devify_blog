package cyou.devify.blog.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import cyou.devify.blog.entities.NewArticleNotificationSubscription;

public interface NewArticleNotificationSubscriptionRepository
    extends JpaRepository<NewArticleNotificationSubscription, UUID> {

  NewArticleNotificationSubscription findByEmail(String email);

  List<EmailProjection> findAllBy();

  public static final record EmailProjection(String email) {
  }
}
