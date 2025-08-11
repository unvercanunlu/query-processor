package tr.unvercanunlu.query_processor.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfig {

  // public static final String WORD_SEPARATOR = " "
  public static final String WORD_SEPARATOR = "\\s+";

  public static final String LINE_SEPARATOR = ";";

}
