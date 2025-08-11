package tr.unvercanunlu.practice.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tr.unvercanunlu.practice.util.TextUtil;

@RequiredArgsConstructor
public enum Command {

  INSERT(true, true),
  DELETE(true, false),
  UPDATE(true, true),
  COMMIT(false, false),
  ROLLBACK(false, false);

  @Getter
  private final boolean nameRequired;

  @Getter
  private final boolean valueRequired;

  private static final Map<String, Command> pairs;

  static {
    pairs = new HashMap<>();

    Arrays.stream(Command.values())
        .forEach(command -> pairs.put(command.name().toUpperCase(), command));
  }

  public static Optional<Command> of(String text) {
    if (text == null) {
      return Optional.empty();
    }

    text = TextUtil.normalize(text);

    if (text.isEmpty()) {
      return Optional.empty();
    }

    text = text.toUpperCase();

    return Optional.ofNullable(pairs.get(text));
  }

}
