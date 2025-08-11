package tr.unvercanunlu.query_processor.model;

public record Query(
    Command command,

    String name,
    Integer value,

    String original
) {


}
