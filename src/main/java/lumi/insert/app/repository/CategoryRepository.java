package lumi.insert.app.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import lumi.insert.app.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    boolean existsByName(String name);

    Slice<Category> findAllByNameContainingAndIsActiveTrue(String name, Pageable pageable);

    Slice<Category> findAllByIsActiveTrue(Pageable pageable);

    Slice<Category> findAllByIsActiveFalse(Pageable pageable);
}
