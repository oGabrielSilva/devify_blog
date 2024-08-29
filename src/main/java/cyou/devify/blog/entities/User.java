package cyou.devify.blog.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cyou.devify.blog.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "UserEntity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(length = 50, nullable = false)
  private String name = "";

  @Column(length = 25)
  private String pseudonym;

  @Column(name = "avatarURL")
  private String avatarURL;
  private String avatarFilePath;

  private String bio;

  @Column(unique = true, length = 150, nullable = false)
  private String email;

  private Role authority = Role.COMMON;

  @OneToOne(cascade = CascadeType.ALL)
  private Social social = new Social();

  @Column(nullable = false)
  private String password;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;

  private boolean enabled = true;

  public User(String name, String email, String avatarURL, String password) {
    this.name = name;
    this.email = email;
    this.avatarURL = avatarURL;
    this.password = password;
  }

  public List<Role> allRoles() {
    var roles = new ArrayList<Role>();
    switch (authority) {
      case ROOT:
        roles.addAll(List.of(Role.ROOT,
            Role.ADMIN,
            Role.MODERATOR,
            Role.HELPER,
            Role.EDITOR));
        break;
      case ADMIN:
        roles.addAll(List.of(Role.ADMIN,
            Role.MODERATOR,
            Role.HELPER,
            Role.EDITOR));
        break;
      case MODERATOR:
        roles.add(Role.MODERATOR);
        roles.add(Role.HELPER);
        roles.add(Role.EDITOR);
        break;
      case HELPER:
        roles.add(Role.HELPER);
        roles.add(Role.EDITOR);
        break;
      case COMMON:
        break;
      default:
        roles.add(Role.EDITOR);
        break;
    }
    roles.add(Role.COMMON);
    return roles;
  }

  public List<String> allRolesAsStrings() {
    return allRoles().stream().map(role -> role.asString()).toList();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    var roles = new ArrayList<GrantedAuthority>();
    switch (authority) {
      case ROOT:
        roles.addAll(List.of(new SimpleGrantedAuthority("ROLE_" + Role.ROOT.asString()),
            new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.asString()),
            new SimpleGrantedAuthority("ROLE_" + Role.MODERATOR.asString()),
            new SimpleGrantedAuthority("ROLE_" + Role.HELPER.asString()),
            new SimpleGrantedAuthority("ROLE_" + Role.EDITOR.asString())));
        break;
      case ADMIN:
        roles.addAll(List.of(new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.asString()),
            new SimpleGrantedAuthority("ROLE_" + Role.MODERATOR.asString()),
            new SimpleGrantedAuthority("ROLE_" + Role.HELPER.asString()),
            new SimpleGrantedAuthority("ROLE_" + Role.EDITOR.asString())));
        break;
      case MODERATOR:
        roles.add(new SimpleGrantedAuthority("ROLE_" + Role.MODERATOR.asString()));
        roles.add(new SimpleGrantedAuthority("ROLE_" + Role.HELPER.asString()));
        roles.add(new SimpleGrantedAuthority("ROLE_" + Role.EDITOR.asString()));
        break;
      case HELPER:
        roles.add(new SimpleGrantedAuthority("ROLE_" + Role.HELPER.asString()));
        roles.add(new SimpleGrantedAuthority("ROLE_" + Role.EDITOR.asString()));
        break;
      case COMMON:
        break;
      default:
        roles.add(new SimpleGrantedAuthority("ROLE_" + Role.EDITOR.asString()));
        break;
    }
    roles.add(new SimpleGrantedAuthority("ROLE_" + Role.COMMON.asString()));
    return roles;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

}
