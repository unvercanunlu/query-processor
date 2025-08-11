package tr.unvercanunlu.query_processor.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import tr.unvercanunlu.query_processor.config.AppConfig;
import tr.unvercanunlu.query_processor.model.Command;
import tr.unvercanunlu.query_processor.model.Query;
import tr.unvercanunlu.query_processor.service.QueryProcessor;
import tr.unvercanunlu.query_processor.util.TextUtil;

public class QueryProcessorImpl implements QueryProcessor {

  private Map<String, Integer> committed = new HashMap<>();
  private Map<String, Integer> staged = new HashMap<>();
  private boolean matched = true;

  @Override
  public Query parse(String line) {
    if (line == null) {
      throw new IllegalArgumentException("Line missing!");
    }

    line = TextUtil.normalize(line);
    if (line.isEmpty()) {
      throw new IllegalArgumentException("Empty line!");
    }

    String[] parts = line.split(AppConfig.WORD_SEPARATOR);

    String commandText = TextUtil.normalize(parts[0]);

    Optional<Command> optionalCommand = Command.of(commandText);
    if (optionalCommand.isEmpty()) {
      throw new IllegalStateException("Unimplemented command command: command=%s line=%s".formatted(commandText, line));
    }

    Command command = optionalCommand.get();

    String name = null;
    Integer value = null;

    if (parts.length >= 2) {
      name = TextUtil.normalize(parts[1]);
    }

    if (parts.length >= 3) {
      String valueText = TextUtil.normalize(parts[2]);

      try {
        value = Integer.parseInt(valueText);
      } catch (Exception e) {
        throw new IllegalStateException("Value cannot be parsed: value=%s".formatted(valueText), e);
      }
    }

    if (parts.length >= 4) {
      throw new IllegalStateException("Unimplemented query command: query=%s".formatted(line));
    }

    return new Query(command, name, value, line);
  }

  @Override
  public void validate(Query query) {
    if (query == null) {
      throw new IllegalArgumentException("Query missing!");
    }

    boolean hasName = (query.name() != null) && !query.name().isBlank();
    boolean hasValue = query.value() != null;

    if (query.command() == null) {
      throw new IllegalArgumentException("Type missing: query=%s".formatted(query.original()));
    }

    if (query.command().isNameRequired() && !hasName) {
      throw new IllegalArgumentException("Name required: query=%s".formatted(query.original()));
    }

    if (query.command().isValueRequired() && !hasValue) {
      throw new IllegalArgumentException("Value required: query=%s".formatted(query.original()));
    }

    if (!query.command().isNameRequired() && hasName) {
      throw new IllegalArgumentException("Name not allowed: query=%s".formatted(query.original()));
    }

    if (!query.command().isValueRequired() && hasValue) {
      throw new IllegalArgumentException("Value not allowed: query=%s".formatted(query.original()));
    }
  }

  @Override
  public void process(Query query) {
    validate(query);

    if (matched) {
      staged = new HashMap<>(committed);
    }

    staged = processHelper(query, staged);
  }

  @Override
  public void processBulk(List<Query> queries) {
    if (queries == null) {
      throw new IllegalStateException("Queries missing!");
    }

    if (matched) {
      staged = new HashMap<>(committed);
    }

    for (Query query : queries) {
      validate(query);
      staged = processHelper(query, staged);
    }
  }

  private Map<String, Integer> processHelper(Query query, Map<String, Integer> state) {
    switch (query.command()) {
      case INSERT -> {
        if (state.containsKey(query.name())) {
          throw new IllegalStateException("Name already exists: name=%s query=%s".formatted(query.name(), query.original()));
        }

        state.put(query.name(), query.value());
        matched = false;
      }

      case UPDATE -> {
        if (!state.containsKey(query.name())) {
          throw new IllegalStateException("Name doesn't exist: name=%s query=%s".formatted(query.name(), query.original()));
        }

        state.put(query.name(), query.value());
        matched = false;
      }

      case DELETE -> {
        if (!state.containsKey(query.name())) {
          throw new IllegalStateException("Name doesn't exist: name=%s query=%s".formatted(query.name(), query.original()));
        }

        state.remove(query.name());
        matched = false;
      }
      case COMMIT -> {
        committed = new HashMap<>(state);
        matched = true;
      }

      case ROLLBACK -> {
        state = new HashMap<>(committed);
        matched = true;
      }

      case null, default -> throw new IllegalStateException("Unimplemented query command: query=%s".formatted(query.original()));
    }

    return state;
  }

}
