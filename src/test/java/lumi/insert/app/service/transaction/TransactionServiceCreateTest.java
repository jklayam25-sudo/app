package lumi.insert.app.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lumi.insert.app.dto.request.TransactionCreateRequest;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Customer;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.nondatabase.TransactionStatus; 
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;

public class TransactionServiceCreateTest extends BaseTransactionServiceTest{
    
    @Test
    @DisplayName("Should return TransactionResponse DTO when creating transaction is successful")
    public void createTransaction_validRequest_returnTransactionResponse(){
        Customer customer = Customer.builder()
        .id(UUID.randomUUID())
        .build();

        when(transactionRepositoryMock.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(customerRepositoryMock.findById(customer.getId())).thenReturn(Optional.of(customer));

        TransactionResponse transaction = transactionServiceMock.createTransaction(TransactionCreateRequest.builder().customerId(customer.getId()).build());
 
        assertNotNull(transaction.invoiceId());
        assertEquals(0, transaction.grandTotal());
        assertEquals(TransactionStatus.PENDING, transaction.status());
    }

    @Test
    @DisplayName("Should throw notFound when customer not found")
    public void createTransaction_invalidCustomer_throwNotFound(){ 
        Customer customer = Customer.builder()
        .id(UUID.randomUUID())
        .build();

        when(customerRepositoryMock.findById(customer.getId())).thenReturn(Optional.empty());

       assertThrows(NotFoundEntityException.class, () -> transactionServiceMock.createTransaction(TransactionCreateRequest.builder().customerId(customer.getId()).build()));
 
    }

    @Test
    @DisplayName("Should throw Tranaction validation exc when customer is inactive")
    public void createTransaction_inactiveCustomer_throwTransactionValidationj(){ 
        Customer customer = Customer.builder()
        .id(UUID.randomUUID())
        .isActive(false)
        .build();

        when(customerRepositoryMock.findById(customer.getId())).thenReturn(Optional.of(customer));

       assertThrows(TransactionValidationException.class, () -> transactionServiceMock.createTransaction(TransactionCreateRequest.builder().customerId(customer.getId()).build()));
 
    }
}
