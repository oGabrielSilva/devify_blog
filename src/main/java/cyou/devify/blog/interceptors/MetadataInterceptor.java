package cyou.devify.blog.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MetadataInterceptor implements HandlerInterceptor {

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView mv) throws Exception {
    if (mv != null) {
      mv.addObject("pageURL", "https://devify.cyou" + request.getRequestURI());
    }
  }

}
