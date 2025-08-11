package tr.unvercanunlu.practice.service;

import java.util.List;
import tr.unvercanunlu.practice.model.Query;

public interface QueryProcessor {

  Query parse(String line);

  void validate(Query query);

  void process(Query query);

  void processBulk(List<Query> queries);

}
