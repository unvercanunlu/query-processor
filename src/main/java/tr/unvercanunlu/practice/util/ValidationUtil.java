package tr.unvercanunlu.practice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ValidationUtil {

  public static void validateArguments(String[] arguments) {
    if (arguments == null || arguments.length == 0 || arguments[0] == null || arguments[0].isEmpty()) {
      throw new IllegalArgumentException("Query missing!");
    }
  }

}
