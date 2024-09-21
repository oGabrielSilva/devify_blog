package cyou.devify.blog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import cyou.devify.blog.entities.Article;
import cyou.devify.blog.entities.User;
import cyou.devify.blog.repositories.NewArticleNotificationSubscriptionRepository;
import cyou.devify.blog.utils.DateFormatter;
import jakarta.mail.Address;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender emailSender;
  @Autowired
  NewArticleNotificationSubscriptionRepository subscriptionRepository;

  @Value("${props.app.domain}")
  String appDomain;

  public boolean sendSimpleMessage(String to, String subject, String text) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("noreply@devify.cyou");
      message.setTo(to);
      message.setSubject(subject);
      message.setText(text);
      emailSender.send(message);
      return true;
    } catch (MailException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean sendMessageToAllSubscriptionNewArticle(Article article, User editor) {
    try {
      var subscription = subscriptionRepository.findAllBy();

      if (subscription.isEmpty()) {
        return true;
      }

      DateFormatter dateFormatter = new DateFormatter();
      MimeMessage message = emailSender.createMimeMessage();
      message.setFrom(new InternetAddress("noreply@devify.cyou"));

      // message.setTo(String.join("; ", subscription.stream().map(sub ->
      // sub.email()).collect(Collectors.toList())));
      Address[] recipients = subscription.stream().map(sub -> {
        try {
          return new InternetAddress(sub.email());
        } catch (AddressException e) {
          e.printStackTrace();
        }
        return null;
      }).toArray(Address[]::new);
      message.setRecipients(RecipientType.TO,
          recipients);
      message.setSubject("Notificação do artigo [articleTitle]".replace("[articleTitle]", article.getTitle()));
      message.setContent(newArticleSubscriptionTemplate.replace("[articleTitle]", article.getTitle())
          .replace("[editor]", editor.getProcessedName())
          .replace("[publishedAt]", dateFormatter.simple(article.getPublishedAt()))
          .replace("[description]", article.getMetaDescription())
          .replace("[unsubscription]", appDomain + "/subscription/notification/cancel").replace("[link]",
              appDomain + String.format("/item/%s/%s", article.getStack().getSlug(), article.getSlug())),
          "text/html; charset=utf-8");

      emailSender.send(message);

      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private static final String newArticleSubscriptionTemplate = """
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
      <html>
        <head>
          <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
          <title>Notificação do artigo [articleTitle]</title>
          <style type="text/css">
            body {
              font-family: Arial, sans-serif;
              background-color: #1f2229;
              margin: 0;
              padding: 0;
            }
            table {
              width: 100%;
              border-collapse: collapse;
            }
            .container {
              width: 100%;
              max-width: 600px;
              margin: 0 auto;
              background-color: #14161a;
              border: 1px solid #353a46;
            }
            .header {
              color: white;
              padding: 10px;
              text-align: center;
            }
            .content {
              padding: 20px;
              color: #abb1bf;
              line-height: 1.5;
            }
            .button {
              display: block;
              margin: 0 auto;
              padding: 10px;
              background-color: #4258ff;
              color: white;
              text-align: center;
              text-decoration: none;
              font-weight: bold;
              border-radius: 16px;
            }
            .footer {
              text-align: center;
              padding: 10px;
              font-size: 12px;
              color: #abb1bf;
            }
          </style>
        </head>
        <body>
          <table>
            <tr>
              <td>
                <div class="container">
                  <div class="header">
                    <h1>Novo Artigo Publicado!</h1>
                  </div>

                  <div class="content">
                    <p>Olá,</p>
                    <p>Acabamos de publicar um novo artigo no blog que pode ser de seu interesse</p>
                    <h2><b style="color: #ffffff">[articleTitle]</b></h2>
                    <p>Escrito por: [editor]</p>
                    <p>Publicado em: [publishedAt]</p>
                    <p>[description]</p>

                    <a href="[link]" style="color: #ffffff" class="button">Leia o artigo</a>
                    <p>
                      <a href="[link]" style="text-align: center; display: block; margin: 0 auto">
                        [link]
                      </a>
                    </p>
                  </div>

                  <div class="footer">
                    <p>
                      Você está recebendo este e-mail porque se inscreveu para receber atualizações da
                      Devify.
                    </p>
                    <p><a href="[unsubscription]">Cancelar inscrição</a></p>
                  </div>
                </div>
              </td>
            </tr>
          </table>
        </body>
      </html>
      """;
}
