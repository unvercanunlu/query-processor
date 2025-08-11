package tr.unvercanunlu.practice.model;

public record Query(
    Command command,

    String name,
    Integer value,

    String original
) {


}
