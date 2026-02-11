package lumi.insert.app.repository;

import java.time.LocalDateTime;
import java.util.List; 
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lumi.insert.app.entity.Product;
import lumi.insert.app.repository.projection.ProductRefreshProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{

    @Query(value = "SELECT p.stockQuantity FROM products p WHERE p.id = :id")
    Optional<Long> getStockById(@Param("id") Long id); 

    Set<Product> findAllByNameContaining(String name);

    Slice<Product> findAllByNameContainingAndIsActiveTrue(String name, Pageable pageable);

    Slice<Product> findAllBy(Pageable pageable);

    Boolean existsByName(String name);

    @Query(value = "SELECT p.id AS id, p.sellPrice AS sellPrice, p.stockQuantity AS stockQuantity FROM products p WHERE p.id IN :ids AND p.updatedAt >= :updatedAt")
    List<ProductRefreshProjection> searchIdUpdatedAtMoreThan(@Param("ids") List<Long> ids,@Param("updatedAt") LocalDateTime updatedAt); 

    @Query(value = "SELECT p FROM products p WHERE p.id IN :ids AND p.updatedAt >= :updatedAt")
    List<Product> searchProductUpdatedAtMoreThan(@Param("ids") List<Long> ids,@Param("updatedAt") LocalDateTime updatedAt); 

}
