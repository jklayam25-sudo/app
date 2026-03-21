package lumi.insert.app.core.repository;

import java.util.Optional;
import java.util.UUID;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lumi.insert.app.core.entity.Supply; 

@Repository
public interface SupplyRepository extends JpaRepository<Supply, UUID>, JpaSpecificationExecutor<Supply>{
    
    Optional<Supply> findByInvoiceId(String invoiceId);

    @Query("SELECT DISTINCT s " + 
        "FROM supplies s LEFT JOIN FETCH " +
        "s.supplyItems si LEFT JOIN FETCH " +
        "si.product " +
        "WHERE s.id = :id " +
        "ORDER BY s.createdAt ASC")
    Optional<Supply> findByIdDetail(@Param("id") UUID id);
 
};
