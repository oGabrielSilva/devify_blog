package cyou.devify.blog.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class UploadFileResult {
  private final String uri;
  private final String origin;

  public UploadFileResult(String uri, String origin) {
    this.uri = uri;
    this.origin = origin;
  }

}
