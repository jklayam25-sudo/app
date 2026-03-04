package lumi.insert.app.repository;
 
import java.util.List; 
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lumi.insert.app.entity.SupplyItem; 

@Repository
public interface SupplyItemRepository extends JpaRepository<SupplyItem, UUID>{
    
    List<SupplyItem> findBySupplyIdAndProductId(UUID supplyId, Long productId);

    Slice<SupplyItem> findAllBySupplyId(UUID supplyId, Pageable pageable);

};
