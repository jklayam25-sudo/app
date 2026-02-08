package lumi.insert.app.repository;
 
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
 
import lumi.insert.app.entity.TransactionPayment;

@Repository
public interface TransactionPaymentRepository extends  JpaRepository<TransactionPayment, UUID>, JpaSpecificationExecutor<TransactionPayment>{
    
    Slice<TransactionPayment> findAllByTransactionId(UUID transactionId, Pageable pageable);

}
