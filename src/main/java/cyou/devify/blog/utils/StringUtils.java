package cyou.devify.blog.utils;

import com.github.slugify.Slugify;

public class StringUtils {

  public static boolean isEquals(String test, String compare) {
    return test != null && test.equals(compare);
  }

  public static boolean isNotEquals(String test, String compare) {
    return !isEquals(test, compare);
  }

  public static boolean isNullOrBlank(String test) {
    return test == null || test.isBlank();
  }

  public static boolean isNonNullOrBlank(String test) {
    return test != null && !test.isBlank();
  }

  public static boolean endsWith(String str, String suffix) {
    return str != null && suffix != null && str.endsWith(suffix);
  }

  public static String removeEnd(String str, String remove) {
    return endsWith(str, remove) ? str.substring(0, str.length() - remove.length()) : str;
  }

  public static String slugify(String content) {
    // https://github.com/slugify/slugify
    return Slugify.builder().build().slugify(content);
  }

}
