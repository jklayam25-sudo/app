package lumi.insert.app.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lumi.insert.app.dto.response.StockCardResponse;
import lumi.insert.app.entity.StockCard; 

public interface StockCardRepository extends JpaRepository<StockCard, UUID>, JpaSpecificationExecutor<StockCard>{
    
    @Query("SELECT s.id as id, s.referenceId as referenceId, s.product.id as productId, s.productName as productName, s.quantity as quantity, s.oldStock as oldStock, s.newStock as newStock, s.basePrice as basePrice, s.type as type, s.description as description, s.createdAt as createdAt " + 
        "FROM stock_cards s WHERE " +
        "s.createdAt between :minTime and :maxTime " +
        "AND :lastId IS NULL OR s.id > :lastId " +
        "ORDER BY s.createdAt ASC")
    Slice<StockCardResponse> findByIndexPagination(@Param("minTime") LocalDateTime minTime,
                                        @Param("maxTime") LocalDateTime maxTime, 
                                        @Param("lastId") UUID lastId, 
                                        Pageable pageable);

    Slice<StockCard> findAllByReferenceId(UUID refId);
 
}
