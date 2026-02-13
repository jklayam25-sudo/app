package lumi.insert.app.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lumi.insert.app.dto.request.TransactionCreateRequest;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.nondatabase.TransactionStatus;

public class TransactionServiceCreateTest extends BaseTransactionServiceTest{
    
    @Test
    @DisplayName("Should return TransactionResponse DTO when creating transaction is successful")
    public void createTransaction_validRequest_returnTransactionResponse(){
        when(transactionRepositoryMock.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        TransactionResponse transaction = transactionServiceMock.createTransaction(TransactionCreateRequest.builder().customerId(null).staffId(null).build());
 
        assertNotNull(transaction.invoiceId());
        assertEquals(0, transaction.grandTotal());
        assertEquals(TransactionStatus.PENDING, transaction.status());
    }
}
