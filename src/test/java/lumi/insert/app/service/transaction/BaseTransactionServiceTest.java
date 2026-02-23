package lumi.insert.app.service.transaction;
 
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.entity.TransactionPayment;
import lumi.insert.app.repository.CustomerRepository;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.TransactionRepository; 
import lumi.insert.app.service.implement.TransactionServiceImpl;
import lumi.insert.app.utils.generator.InvoiceGenerator;
import lumi.insert.app.utils.mapper.AllTransactionMapper;
import lumi.insert.app.utils.mapper.AllTransactionMapperImpl;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public abstract class BaseTransactionServiceTest {
    
    @InjectMocks
    TransactionServiceImpl transactionServiceMock;

    @Mock
    TransactionRepository transactionRepositoryMock;

    @Mock
    ProductRepository productRepositoryMock;

    @Mock
    CustomerRepository customerRepositoryMock;

    @Spy
    AllTransactionMapper allTransactionMapper = new AllTransactionMapperImpl();

    @Spy
    InvoiceGenerator invoiceGenerator = new InvoiceGenerator();

    
    public Transaction setupTransaction;

    public TransactionPayment setupTransactionPayment;

    public TransactionItem setupTransactionItem;

    public Product setupProduct;

    @BeforeEach
    void setUp(){
        setupTransactionPayment = TransactionPayment.builder()
        .id(UUID.randomUUID())
        .build();

        setupTransaction = Transaction.builder()
        .id(UUID.randomUUID())
        .build();

        setupTransactionItem = TransactionItem.builder()
        .id(UUID.randomUUID())
        .build();

        setupProduct = Product.builder()
        .id(1L)
        .build();
    }
}
