package cyou.devify.blog.utils;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtils {
  public static String getIp(HttpServletRequest request) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (StringUtils.isNullOrBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
    }
    return ipAddress;
  }
}
