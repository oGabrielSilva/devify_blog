package cyou.devify.blog.configurations;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {
  @Value("${props.mail.host}")
  String host;
  @Value("${props.mail.port}")
  int port;
  @Value("${props.mail.user}")
  String username;
  @Value("${props.mail.password}")
  String password;
  @Value("${props.mail.debug}")
  boolean debug;
  @Value("${props.mail.smtp.auth}")
  boolean smtpAuth;
  @Value("${props.mail.smtp.starttls-enable}")
  boolean starttlsEnable;

  @Bean
  JavaMailSender getJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(port);

    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", smtpAuth ? "true" : "false");
    props.put("mail.smtp.starttls.enable", starttlsEnable ? "true" : "false");
    props.put("mail.debug", debug ? "true" : "false");

    return mailSender;
  }
}
