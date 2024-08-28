package cyou.devify.blog.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cyou.devify.blog.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component - removido
public class UserToModelViewInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView mv) throws Exception {
        var user = userService.getCurrentAuthenticatedUser();

        mv.addObject("user", user);
    }
}