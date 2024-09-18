package cyou.devify.blog.entities;

import java.util.UUID;

import cyou.devify.blog.enums.Role;

public record MinimizedUser(UUID id, String username, String name, String pseudonym, String avatarURL,
        Role authority) {

}
