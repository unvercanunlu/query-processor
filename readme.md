**Query Processor**
A simple Java-based query processing engine supporting **INSERT**, **UPDATE**, **DELETE**, **COMMIT**, and **ROLLBACK** commands with parsing, validation, and staged/committed state management.

**Features**

* Command parsing from raw text input (`parse`)
* Validation of required/forbidden name & value fields
* Staged vs. committed state tracking (`COMMIT` / `ROLLBACK`)
* Bulk query processing (`processBulk`)
* Configurable word & line separators (`AppConfig`)
* Clear exception messages for invalid queries

**Configuration (`AppConfig.java`)**

```java
WORD_SEPARATOR = "\\s+";
LINE_SEPARATOR = ";";
```

**API**

```java
Query parse(String line);
void validate(Query query);
void process(Query query);
void processBulk(List<Query> queries);
```

**Example**

```java
QueryProcessor processor = new QueryProcessorImpl();
List<Query> queries = List.of(
    processor.parse("INSERT apple 10"),
    processor.parse("COMMIT")
);
processor.processBulk(queries);
```
