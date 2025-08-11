package tr.unvercanunlu.practice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextUtil {

  public static String normalize(String text) {
    if (text == null) {
      return "";
    }

    return text.trim();
  }

}
