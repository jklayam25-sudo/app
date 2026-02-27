package lumi.insert.app.service.implement;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.StockCardCreateRequest;
import lumi.insert.app.dto.request.StockCardGetByFilter;
import lumi.insert.app.dto.response.StockCardResponse;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.StockCard;
import lumi.insert.app.entity.nondatabase.StockMove;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.StockCardRepository;
import lumi.insert.app.repository.TransactionItemRepository;
import lumi.insert.app.service.StockCardService;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.StockCardMapper;

@Service
@Transactional
@Slf4j
public class StockCardServiceImpl implements StockCardService{

    @Autowired
    StockCardRepository stockCardRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TransactionItemRepository transactionItemRepository;

    @Autowired
    StockCardMapper stockCardMapper;

    @Autowired
    JpaSpecGenerator jpaSpecGenerator;

    @Override
    public StockCardResponse createStockCard(StockCardCreateRequest request) {
        if((request.getType() == StockMove.CUSTOMER_IN.toString() || request.getType() == StockMove.SUPPLIER_IN.toString() ||
            request.getType() == StockMove.REPAIRED.toString()) && request.getQuantity() < 0L) {
                throw new TransactionValidationException("");
        }

        if((request.getType() == StockMove.CUSTOMER_OUT.toString() || request.getType() == StockMove.SUPPLIER_OUT.toString() ||
            request.getType() == StockMove.DEFECT.toString()) && request.getQuantity() > 0L) {
                throw new TransactionValidationException("");
        }

        if((request.getType() == StockMove.CUSTOMER_IN.toString() || request.getType() == StockMove.CUSTOMER_OUT.toString()) && !transactionItemRepository.existsById(request.getReferenceId())) {
            throw new NotFoundEntityException("");
        }

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new NotFoundEntityException(null));

        Long oldStock = product.getStockQuantity();

        product.setStockQuantity(oldStock + request.getQuantity());
 
        if(product.getStockQuantity() < 0) throw new TransactionValidationException("Product stocks with ID " + request.getProductId() + " doesn't meet buyer quantity, stock left: " + oldStock);

        StockCard stockCard = StockCard.builder()
        .referenceId(request.getReferenceId())
        .product(product)
        .productName(product.getName())
        .quantity(request.getQuantity())
        .oldStock(oldStock)
        .newStock(product.getStockQuantity())
        .type(StockMove.valueOf(request.getType()))
        .basePrice(product.getBasePrice())
        .build();

        StockCard savedStockCard = stockCardRepository.save(stockCard);
        
        StockCardResponse stockCardResponse = stockCardMapper.createDtoResponseFromStockCard(savedStockCard); 
        return stockCardResponse;
    }

    @Override
    public StockCardResponse getStockCard(UUID id) {
        StockCard stockCard = stockCardRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException(null));

        return stockCardMapper.createDtoResponseFromStockCard(stockCard);
    }

    @Override
    public Slice<StockCardResponse> getStockCards(UUID lastId, PaginationRequest request) {
        Pageable pageable = jpaSpecGenerator.pageable(request);

        Slice<StockCardResponse> slices = stockCardRepository.findByIndexPagination(LocalDateTime.of(1900, 10, 10, 10, 10), LocalDateTime.of(3000, 10, 10, 10, 10), lastId, pageable);

        return slices;
    }

    // @Override
    // public Slice<StockCardResponse> getStockCardByProductId(Long id, UUID lastId, PaginationRequest request) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getStockCardByProductId'");
    // }

    @Override
    public Slice<StockCardResponse> searchStockCards(StockCardGetByFilter request) {
        Pageable pageable = jpaSpecGenerator.pageable(request);

        Specification<StockCard> stockCardSpecification = jpaSpecGenerator.stockCardSpecification(request);

        Slice<StockCard> slices = stockCardRepository.findAll(stockCardSpecification, pageable);

        return slices.map(stockCardMapper::createDtoResponseFromStockCard);
    }
    
}
