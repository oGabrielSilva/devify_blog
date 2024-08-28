package cyou.devify.blog.vm;

import java.time.Instant;

public record ExceptionResponseViewModel(Instant timestamp, String message, String url, int status) {

}
