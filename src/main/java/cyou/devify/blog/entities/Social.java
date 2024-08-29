package cyou.devify.blog.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Social {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String instagram;
  private String youtube;
  private String twitter;
  private String github;
  private String discord;
  private String linkedin;
  private String site;
}
