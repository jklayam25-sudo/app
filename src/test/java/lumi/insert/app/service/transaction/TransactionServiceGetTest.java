package lumi.insert.app.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice; 
import org.springframework.data.jpa.domain.Specification;

import lumi.insert.app.dto.request.TransactionGetByFilter;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.exception.NotFoundEntityException;

public class TransactionServiceGetTest extends BaseTransactionServiceTest{
    
    @Test
    @DisplayName("Should thrown NotFoundEntity Exception when finding transaction result in none at DB")
    public void getTransaction_invalidId_thrownNotFoundEntity(){
        UUID randomUUID = UUID.randomUUID();
        when(transactionRepositoryMock.findById(randomUUID)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> transactionServiceMock.getTransaction(randomUUID));
    }

    @Test
    @DisplayName("Should return TransactionResponse DTO when finding transaction result in succeed")
    public void getTransaction_validId_returnTransactionResponse(){
        UUID randomUUID = UUID.randomUUID();
        Transaction mockTransaction = Transaction.builder()
        .id(randomUUID)
        .build();

        when(transactionRepositoryMock.findById(randomUUID)).thenReturn(Optional.of(mockTransaction));

        TransactionResponse transaction = transactionServiceMock.getTransaction(randomUUID);
        assertEquals(randomUUID, transaction.id());
    }

    @Test
    @DisplayName("Should return Slice of filtered TransactionResponse DTO when finding transaction result in succeed")
    public void searchTransactionsByRequests_validFilter_returnSliceTransactionResponse(){
        UUID randomUUID = UUID.randomUUID();
        Transaction mockTransaction = Transaction.builder()
        .id(randomUUID)
        .build();

        Page<Transaction> transactions = new PageImpl<>(List.of(mockTransaction));

        when(transactionRepositoryMock.findAll(any(Specification.class), any(Pageable.class))).thenReturn(transactions);

        Slice<TransactionResponse> result = transactionServiceMock.searchTransactionsByRequests(TransactionGetByFilter.builder().build());
        assertEquals(1, result.getNumberOfElements());
        assertEquals(randomUUID, result.getContent().getFirst().id());
    }

}
