package cyou.devify.blog.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cyou.devify.blog.filters.ServletRequestWrapperFilter;

@Configuration
public class RequestConfigurer implements WebMvcConfigurer {
  @Bean
  ServletRequestWrapperFilter getServletRequestWrapperFilter() {
    return new ServletRequestWrapperFilter();
  }

}
