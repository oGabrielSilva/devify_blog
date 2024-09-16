package cyou.devify.blog.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cyou.devify.blog.filters.ServletRequestWrapperFilter;
import cyou.devify.blog.interceptors.MetadataInterceptor;

@Configuration
public class RequestConfigurer implements WebMvcConfigurer {
  @Autowired
  MetadataInterceptor metadataInterceptor;

  @Bean
  ServletRequestWrapperFilter getServletRequestWrapperFilter() {
    return new ServletRequestWrapperFilter();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(metadataInterceptor).excludePathPatterns(
        "static/images/**",
        "/site.webmanifest",
        "/about.txt",
        "/favicon.ico",
        "/favicon-16x16.png",
        "/favicon-32x32.png",
        "/android-chrome-192x192.png",
        "/android-chrome-512x512.png",
        "/apple-touch-icon.png",
        "/images/**",
        "/javascript/**",
        "/css/**");
  }

}
