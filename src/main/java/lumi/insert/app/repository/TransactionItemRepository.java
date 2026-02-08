package lumi.insert.app.repository;
 
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import lumi.insert.app.entity.TransactionItem;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionItem, UUID>{
    
    Optional<TransactionItem> findByTransactionIdAndProductId(UUID transactionId, Long productId);

    Slice<TransactionItem> findAllByTransactionId(UUID transactionId, Pageable pageable);

};
