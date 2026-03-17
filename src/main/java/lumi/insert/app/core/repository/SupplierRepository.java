package lumi.insert.app.core.repository;
 
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import lumi.insert.app.core.entity.Supplier;
import lumi.insert.app.dto.response.SupplierNameResponse; 

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID>, JpaSpecificationExecutor<Supplier>{
    
    boolean existsByName(String name);

    Slice<SupplierNameResponse> getByNameContainingIgnoreCaseAndIdAfter(String name, UUID lastId, Pageable pageable);
}
