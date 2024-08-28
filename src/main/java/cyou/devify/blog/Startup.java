package cyou.devify.blog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Startup {
	@Value("${server.port}")
	int port;

	@EventListener(ApplicationReadyEvent.class)
	public void onStart() {
		System.out.println("\nSystem reserved URLs: /images, /css, /javascript");
		System.out.println(String.format("Application on http://127.0.0.1:%s\n", port));
	}

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}

}
