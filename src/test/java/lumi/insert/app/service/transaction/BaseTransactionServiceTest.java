package lumi.insert.app.service.transaction;
 
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension; 

import lumi.insert.app.repository.TransactionRepository; 
import lumi.insert.app.service.implement.TransactionServiceImpl;
import lumi.insert.app.utils.generator.InvoiceGenerator;
import lumi.insert.app.utils.mapper.AllTransactionMapper;
import lumi.insert.app.utils.mapper.AllTransactionMapperImpl;

@ExtendWith(MockitoExtension.class)
public abstract class BaseTransactionServiceTest {
    
    @InjectMocks
    TransactionServiceImpl transactionServiceMock;

    @Mock
    TransactionRepository transactionRepositoryMock;

    @Spy
    AllTransactionMapper allTransactionMapper = new AllTransactionMapperImpl();

    @Spy
    InvoiceGenerator invoiceGenerator = new InvoiceGenerator();
}
