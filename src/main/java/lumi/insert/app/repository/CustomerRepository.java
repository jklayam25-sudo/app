package lumi.insert.app.repository;
 
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import lumi.insert.app.dto.response.CustomerNameResponse;
import lumi.insert.app.entity.Customer; 

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer>{
    
    boolean existsByName(String name);

    Slice<CustomerNameResponse> getByNameContainingIgnoreCase(String name, Pageable pageable);
}
