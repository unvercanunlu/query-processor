package tr.unvercanunlu.query_processor;

import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import tr.unvercanunlu.query_processor.config.AppConfig;
import tr.unvercanunlu.query_processor.model.Query;
import tr.unvercanunlu.query_processor.service.QueryProcessor;
import tr.unvercanunlu.query_processor.service.impl.QueryProcessorImpl;
import tr.unvercanunlu.query_processor.util.TextUtil;
import tr.unvercanunlu.query_processor.util.ValidationUtil;

@Slf4j
public class App {

  public static void main(String[] args) {
    ValidationUtil.validateArguments(args);

    log.info("Application argument validated.");

    String input = TextUtil.normalize(args[0]);

    QueryProcessor processor = new QueryProcessorImpl();

    List<Query> queries = new LinkedList<>();

    String[] separated = input.split(AppConfig.LINE_SEPARATOR);

    for (String line : separated) {
      line = TextUtil.normalize(line);
      Query query = processor.parse(line);
      queries.add(query);
    }

    log.info("%d lines parsed.".formatted(queries.size()));

    for (Query query : queries) {
      processor.validate(query);
    }

    log.info("%d queries validated.".formatted(queries.size()));

    processor.processBulk(queries);

    log.info("%d queries processed.".formatted(queries.size()));
  }

}
