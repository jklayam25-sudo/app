package lumi.insert.app.service.stockcard;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.StockCardRepository;
import lumi.insert.app.repository.TransactionItemRepository; 
import lumi.insert.app.service.implement.StockCardServiceImpl;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.StockCardMapper;
import lumi.insert.app.utils.mapper.StockCardMapperImpl;

@ExtendWith(MockitoExtension.class)
public abstract class BaseStockCardServiceTest {
    
    @InjectMocks
    StockCardServiceImpl stockCardService;

    @Mock
    StockCardRepository stockCardRepository;

    @Mock
    TransactionItemRepository transactionItemRepository;

    @Mock
    ProductRepository productRepository;

    @Spy
    StockCardMapper stockCardMapper = new StockCardMapperImpl();

    @Spy
    JpaSpecGenerator jpaSpecGenerator = new JpaSpecGenerator();
     

    public Product setupProduct;

    public TransactionItem setupTransactionItem;

    @BeforeEach
    void setUp(){
        setupProduct = Product.builder()
        .id(999L)
        .name("Test product")
        .stockQuantity(10L)
        .basePrice(19000L)
        .build(); 

        setupTransactionItem = TransactionItem.builder()
        .id(UUID.randomUUID())
        .build();
    }
}
