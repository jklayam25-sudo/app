package lumi.insert.app.repository.projection;

public record ProductOutOfStock(
    Long id,
    String name,
    Long stockQuantity,
    Long stockMinimum
) {}
