package lumi.insert.app.repository.projection;

public record ProductRefreshProjection(
    Long id, 
    Long sellPrice, 
    Long stockQuantity) {
}
