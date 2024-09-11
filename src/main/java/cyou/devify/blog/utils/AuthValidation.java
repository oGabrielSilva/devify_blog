package cyou.devify.blog.utils;

public class AuthValidation {
  public static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

  public boolean isEmailValid(String email) {
    return email != null && email.matches(emailRegex) && email.length() <= 150;
  }

  public boolean isPasswordValid(String password) {
    return password != null && password.length() >= 8;
  }

  public boolean isNameValid(String name) {
    return name != null && name.trim().length() >= 2;
  }
}
