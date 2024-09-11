package cyou.devify.blog.enums;

public enum Role {
  COMMON("COMMON"),
  EDITOR("EDITOR"),
  HELPER("HELPER"),
  MODERATOR("MODERATOR"),
  ADMIN("ADMIN"),
  ROOT("ROOT");

  private final String descriptor;

  Role(String role) {
    descriptor = role;
  }

  public String asString() {
    return descriptor;
  }

  public String capitalize() {
    var str = asString().toLowerCase();

    return str.substring(0, 1).toUpperCase().concat(str.substring(1));
  }
}
