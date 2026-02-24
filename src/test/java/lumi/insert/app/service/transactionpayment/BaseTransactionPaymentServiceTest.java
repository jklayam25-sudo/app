package lumi.insert.app.service.transactionpayment;
 
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension; 

import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.entity.TransactionPayment;
import lumi.insert.app.repository.TransactionPaymentRepository;
import lumi.insert.app.repository.TransactionRepository;
import lumi.insert.app.service.implement.TransactionPaymentServiceImpl; 
import lumi.insert.app.utils.generator.InvoiceGenerator;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.AllTransactionMapper;
import lumi.insert.app.utils.mapper.AllTransactionMapperImpl;
 
@ExtendWith(MockitoExtension.class)
public abstract class BaseTransactionPaymentServiceTest {
    
    @InjectMocks
    TransactionPaymentServiceImpl transactionPaymentServiceMock;

    @Mock
    TransactionRepository transactionRepositoryMock;

    @Mock
    TransactionPaymentRepository transactionPaymentRepositoryMock;

    @Mock
    JpaSpecGenerator jpaSpecGenerator;

    @Spy
    AllTransactionMapper allTransactionMapper = new AllTransactionMapperImpl();

    @Spy
    InvoiceGenerator invoiceGenerator = new InvoiceGenerator();

    public Transaction setupTransaction;

    public TransactionPayment setupTransactionPayment;

    public TransactionItem setupTransactionItem;

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
    }
}
