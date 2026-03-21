package lumi.insert.app.core.repository.projection;

public record ProductOutOfStock(
    Long id,
    String name,
    Long stockQuantity,
    Long stockMinimum
) {}
