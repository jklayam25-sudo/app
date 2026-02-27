package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import; 
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.request.StockCardGetByFilter;
import lumi.insert.app.dto.response.StockCardResponse;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.StockCard;
import lumi.insert.app.entity.nondatabase.StockMove;
import lumi.insert.app.utils.generator.JpaSpecGenerator;

@DataJpaTest
@Transactional
@Import(JpaSpecGenerator.class)
public class StockCardRepositoryTest {
    
    @Autowired
    StockCardRepository stockCardRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    JpaSpecGenerator jpaSpecGenerator;

    Product product;

    @BeforeEach
    void setup(){
        product = productRepository.saveAndFlush(Product.builder().name("Shoes").basePrice(1000L).sellPrice(1100L).stockQuantity(10L).build());
    }

    @Test
    void findByIndexPagination_foundEntity_returnSliceDTO(){
        StockCard stockCard1 = StockCard.builder()
        .referenceId(UUID.randomUUID())
        .product(product)
        .productName(product.getName())
        .quantity(-5L)
        .oldStock(10L)
        .newStock(5L)
        .type(StockMove.SALE)
        .basePrice(1000L)
        .build();

        StockCard stockCard2 = StockCard.builder()
        .referenceId(UUID.randomUUID())
        .product(product)
        .productName(product.getName())
        .quantity(-5L)
        .oldStock(10L)
        .newStock(5L)
        .type(StockMove.SALE)
        .basePrice(1000L)
        .build();

        stockCardRepository.saveAndFlush(stockCard1);
        StockCard saveAndFlush2 = stockCardRepository.saveAndFlush(stockCard2);;

        Slice<StockCardResponse> byIndexPagination = stockCardRepository.findByIndexPagination(LocalDateTime.now().minusDays(1), LocalDateTime.now(), null, PageRequest.of(0, 2));
        assertEquals(2, byIndexPagination.getNumberOfElements());
        assertEquals(saveAndFlush2.getId(), byIndexPagination.getContent().getLast().id());
    }

    @Test
    void findByIndexPagination_foundEntityFilterLastId_returnSliceDTO(){
        StockCard stockCard1 = StockCard.builder()
        .referenceId(UUID.randomUUID())
        .product(product)
        .productName(product.getName())
        .quantity(-5L)
        .oldStock(10L)
        .newStock(5L)
        .type(StockMove.SALE)
        .basePrice(1000L)
        .build();

        StockCard stockCard2 = StockCard.builder()
        .referenceId(UUID.randomUUID())
        .product(product)
        .productName(product.getName())
        .quantity(-5L)
        .oldStock(10L)
        .newStock(5L)
        .type(StockMove.SALE)
        .basePrice(1000L)
        .build();

        StockCard saveAndFlush = stockCardRepository.saveAndFlush(stockCard1);
        StockCard saveAndFlush2 = stockCardRepository.saveAndFlush(stockCard2);;

        Slice<StockCardResponse> byIndexPagination = stockCardRepository.findByIndexPagination(LocalDateTime.now().minusDays(1), LocalDateTime.now(), saveAndFlush.getId(), PageRequest.of(0, 2));
        assertEquals(1, byIndexPagination.getNumberOfElements());
        assertEquals(saveAndFlush2.getId(), byIndexPagination.getContent().getLast().id());
    }

    @Test
    void findAllSpecification_foundEntityFilterLastId_returnSliceDTO(){
        StockCard stockCard1 = StockCard.builder()
        .referenceId(UUID.randomUUID())
        .product(product)
        .productName(product.getName())
        .quantity(-5L)
        .oldStock(10L)
        .newStock(5L)
        .type(StockMove.PURCHASE)
        .basePrice(1000L)
        .build();

        StockCard stockCard2 = StockCard.builder()
        .referenceId(UUID.randomUUID())
        .product(product)
        .productName(product.getName())
        .quantity(-5L)
        .oldStock(10L)
        .newStock(5L)
        .type(StockMove.SALE)
        .basePrice(1000L)
        .build();

        List<StockCard> saveAllAndFlush = stockCardRepository.saveAllAndFlush(List.of(stockCard1, stockCard2));;

        StockCardGetByFilter request = StockCardGetByFilter.builder()
        .type(StockMove.PURCHASE)
        .build();

        Pageable pageable = jpaSpecGenerator.pageable(request);
        Specification<StockCard> stockCardSpecification = jpaSpecGenerator.stockCardSpecification(request);

        Slice<StockCard> slices = stockCardRepository.findAll(stockCardSpecification, pageable);;
        assertEquals(1, slices.getNumberOfElements());
        assertTrue(slices.isLast());
        assertEquals(saveAllAndFlush.getFirst().getId(), slices.getContent().getLast().getId());
    }


}
