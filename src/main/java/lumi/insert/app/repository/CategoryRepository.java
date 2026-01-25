package lumi.insert.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lumi.insert.app.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    boolean existsByName(String name);

}
