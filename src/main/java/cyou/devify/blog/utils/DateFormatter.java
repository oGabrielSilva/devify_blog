package cyou.devify.blog.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {
  public String simple(Instant date) {
    DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("d MMM, yyyy", Locale.of("pt", "BR"))
        .withZone(ZoneId.of("America/Sao_Paulo"));

    String formattedDate = formatter.format(date);
    return formattedDate;
  }
}
