package cyou.devify.blog.filters;

import java.io.IOException;

import cyou.devify.blog.utils.StringUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class ServletRequestWrapperFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
      @Override
      public String getRequestURI() {
        String requestURI = super.getRequestURI();
        if (StringUtils.endsWith(requestURI, "/") && requestURI.length() > 1) {
          return StringUtils.removeEnd(requestURI, "/");
        }
        return requestURI;
      }
    };
    chain.doFilter(requestWrapper, response);
  }

}
