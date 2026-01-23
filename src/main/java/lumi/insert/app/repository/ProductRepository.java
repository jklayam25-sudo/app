package lumi.insert.app.repository;

import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lumi.insert.app.entity.Product;
import lumi.insert.app.repository.projection.ProductStockProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    ProductStockProjection getStockById(Long id); 

    Set<Product> findAllByNameContaining(String name);

    Slice<Product> findAllByNameContaining(String name, Pageable pageable);

    Slice<Product> findAllBy(Pageable pageable);
}
