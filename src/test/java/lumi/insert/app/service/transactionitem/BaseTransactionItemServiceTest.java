package lumi.insert.app.service.transactionitem;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.TransactionItemRepository;
import lumi.insert.app.repository.TransactionRepository;
import lumi.insert.app.service.implement.TransactionItemServiceImpl; 
import lumi.insert.app.utils.mapper.AllTransactionMapper;
import lumi.insert.app.utils.mapper.AllTransactionMapperImpl;

@ExtendWith(MockitoExtension.class)
public abstract class BaseTransactionItemServiceTest {
    
    @InjectMocks
    TransactionItemServiceImpl transactionItemServiceMock;

    @Mock
    TransactionRepository transactionRepositoryMock;

    @Mock
    ProductRepository productRepositoryMock;

    @Mock
    TransactionItemRepository transactionItemRepositoryMock;

    @Spy
    AllTransactionMapper allTransactionMapper = new AllTransactionMapperImpl();

    public Transaction setupTransaction;

    public Product setupProduct;

    public TransactionItem setupTransactionItem;

    @BeforeEach
    void setUp(){
        setupProduct = Product.builder()
        .id(999L)
        .stockQuantity(10L)
        .sellPrice(19000L)
        .build();

        setupTransaction = Transaction.builder()
        .id(UUID.randomUUID())
        .build();

        setupTransactionItem = TransactionItem.builder()
        .id(UUID.randomUUID())
        .build();
    }
}
