package tr.unvercanunlu.query_processor.service;

import java.util.List;
import tr.unvercanunlu.query_processor.model.Query;

public interface QueryProcessor {

  Query parse(String line);

  void validate(Query query);

  void process(Query query);

  void processBulk(List<Query> queries);

}
