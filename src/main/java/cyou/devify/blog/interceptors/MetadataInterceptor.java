package cyou.devify.blog.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MetadataInterceptor implements HandlerInterceptor {
  @Value("${props.app.domain}")
  String domain;

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView mv) throws Exception {
    if (mv != null) {
      mv.addObject("domain", domain);
      mv.addObject("basePageURL", request.getRequestURI());

      String pageURL = domain + request.getRequestURI();
      if (pageURL.endsWith("/")) {
        pageURL = pageURL.substring(0, pageURL.length() - 1);
      }

      mv.addObject("pageURL", pageURL);
    }
  }

}
