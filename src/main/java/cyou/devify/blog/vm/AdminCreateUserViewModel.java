package cyou.devify.blog.vm;

import cyou.devify.blog.enums.Role;

public record AdminCreateUserViewModel(
    String name,
    String username,
    String email,
    String password,
    Role authority) {

}
