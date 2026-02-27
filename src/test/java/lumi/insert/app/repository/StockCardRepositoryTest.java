package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.response.StockCardResponse;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.StockCard;
import lumi.insert.app.entity.nondatabase.StockMove;

@DataJpaTest
@Transactional
public class StockCardRepositoryTest {
    
    @Autowired
    StockCardRepository stockCardRepository;

    @Autowired
    ProductRepository productRepository;

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

        List<StockCard> saveAllAndFlush = stockCardRepository.saveAllAndFlush(List.of(stockCard1, stockCard2));;

        Slice<StockCardResponse> byIndexPagination = stockCardRepository.findByIndexPagination(LocalDateTime.now().minusDays(1), LocalDateTime.now(), null, PageRequest.of(0, 2));
        assertEquals(2, byIndexPagination.getNumberOfElements());
        assertEquals(saveAllAndFlush.getLast().getId(), byIndexPagination.getContent().getLast().id());
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

        List<StockCard> saveAllAndFlush = stockCardRepository.saveAllAndFlush(List.of(stockCard1, stockCard2));;

        Slice<StockCardResponse> byIndexPagination = stockCardRepository.findByIndexPagination(LocalDateTime.now().minusDays(1), LocalDateTime.now(), saveAllAndFlush.getFirst().getId(), PageRequest.of(0, 2));
        assertEquals(1, byIndexPagination.getNumberOfElements());
        assertEquals(saveAllAndFlush.getLast().getId(), byIndexPagination.getContent().getLast().id());
    }

    
}
