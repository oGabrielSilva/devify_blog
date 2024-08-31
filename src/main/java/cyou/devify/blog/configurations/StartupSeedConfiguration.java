package cyou.devify.blog.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import cyou.devify.blog.entities.Stack;
import cyou.devify.blog.entities.User;
import cyou.devify.blog.enums.Role;
import cyou.devify.blog.repositories.StackRepository;
import cyou.devify.blog.repositories.UserRepository;

@Component
public class StartupSeedConfiguration implements ApplicationRunner {
  private final String firstUserEmail = "root@devify.cyou";

  @Value("${devify.root.initial.pass}")
  String firstRootPass;
  @Value("${devify.seed-java-stack}")
  boolean seedJavaStack;

  @Autowired
  StackRepository stackRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    User rootUser = userRepository.findByEmail(firstUserEmail);
    if (rootUser == null) {
      var u = new User("Root", firstUserEmail, "/images/avatar_placeholder.png",
          passwordEncoder.encode(firstRootPass));
      u.setAuthority(Role.ROOT);
      rootUser = userRepository.save(u);
      System.out.println(String.format("User seed -> %s. Change your password", firstRootPass));
    }

    if (seedJavaStack && stackRepository.findByName("Java") == null) {
      var java = new Stack(null, "Java",
          "<div><h3><b>What is Java technology and why do I need it?</b></h3><p>Java is a programming language and computing platform first released by Sun Microsystems in 1995. It has evolved from humble beginnings to power a large share of today\'s digital world, by providing the reliable platform upon which many services and applications are built. New, innovative products and digital services designed for the future continue to rely on Java, as well.</p></div>",
          "Java is a programming language and computing platform first released by Sun Microsystems in 1995", false,
          rootUser.getId(), rootUser.getId(), null, null);
      System.out.println("Java stack added");
      stackRepository.save(java);
    }
  }

}
