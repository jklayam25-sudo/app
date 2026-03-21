package lumi.insert.app.core.repository.projection;

public record ProductRefreshProjection(
    Long id, 
    Long sellPrice, 
    Long stockQuantity) {
}
