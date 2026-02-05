package lumi.insert.app.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lumi.insert.app.entity.TransactionStatus;
import lumi.insert.app.exception.BoilerplateRequestException;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Transaction;

public class TransactionServiceEditTest extends BaseTransactionServiceTest{
    
    @Test
    @DisplayName("Should return TransactionResponse DTO when set transaction.status to Process succeed")
    public void setTransactionToProcess_validRequest_returnTransactionResponse(){
        UUID randomUUID = UUID.randomUUID();
        Transaction mockTransaction = Transaction.builder()
        .id(randomUUID)
        .build();

        when(transactionRepositoryMock.findById(randomUUID)).thenReturn(Optional.of(mockTransaction));

        TransactionResponse transaction = transactionServiceMock.setTransactionToProcess(randomUUID);
        assertEquals(TransactionStatus.PROCESS, transaction.status());
    }

    @Test
    @DisplayName("Should throw NotFoundEntityException when id is invalid")
    public void setTransactionToProcess_invalidId_throwNotFound(){
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, ()-> transactionServiceMock.setTransactionToProcess(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should throw BoilerplateException when transaction.status already Process")
    public void setTransactionToProcess_alreadyProcess_throwBoilerPlate(){
        UUID randomUUID = UUID.randomUUID();
        Transaction mockTransaction = Transaction.builder()
        .id(randomUUID)
        .status(TransactionStatus.PROCESS)
        .build();

        when(transactionRepositoryMock.findById(randomUUID)).thenReturn(Optional.of(mockTransaction));

        assertThrows(BoilerplateRequestException.class, ()-> transactionServiceMock.setTransactionToProcess(randomUUID));
    }

    @Test
    @DisplayName("Should throw BoilerplateException when transaction.status isn't pending which caused false logic")
    public void setTransactionToProcess_notFromPending_throwForbbidenRequest(){
        UUID randomUUID = UUID.randomUUID();
        Transaction mockTransaction = Transaction.builder()
        .id(randomUUID)
        .status(TransactionStatus.CANCELLED)
        .build();

        when(transactionRepositoryMock.findById(randomUUID)).thenReturn(Optional.of(mockTransaction));

        assertThrows(ForbiddenRequestException.class, ()-> transactionServiceMock.setTransactionToProcess(randomUUID));
    }

    @Test
    @DisplayName("Should return TransactionResponse DTO when set transaction.status to Complete succeed")
    public void setTransactionToComplete_validRequest_returnTransactionResponse() {
        UUID randomUUID = UUID.randomUUID(); 
        Transaction mockTransaction = Transaction.builder()
                .id(randomUUID)
                .status(TransactionStatus.PROCESS)
                .build();

        when(transactionRepositoryMock.findById(randomUUID)).thenReturn(Optional.of(mockTransaction));

        TransactionResponse result = transactionServiceMock.setTransactionToComplete(randomUUID);
        
        assertEquals(TransactionStatus.COMPLETE, result.status()); 
        verify(transactionRepositoryMock).findById(randomUUID);
    }

    @Test
    @DisplayName("Should throw NotFoundEntityException when id is invalid for Complete")
    public void setTransactionToComplete_invalidId_throwNotFound() {
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> transactionServiceMock.setTransactionToComplete(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should throw BoilerplateException when transaction.status already Complete")
    public void setTransactionToComplete_alreadyComplete_throwBoilerPlate() {
        UUID randomUUID = UUID.randomUUID();
        Transaction mockTransaction = Transaction.builder()
                .id(randomUUID)
                .status(TransactionStatus.COMPLETE)
                .build();

        when(transactionRepositoryMock.findById(randomUUID)).thenReturn(Optional.of(mockTransaction));

        assertThrows(BoilerplateRequestException.class, () -> transactionServiceMock.setTransactionToComplete(randomUUID));
    }

    @Test
    @DisplayName("Should throw ForbiddenRequestException when trying to complete a cancelled or pending transaction")
    public void setTransactionToComplete_invalidTransition_throwForbiddenRequest() {
        UUID randomUUID = UUID.randomUUID(); 
        Transaction mockTransaction = Transaction.builder()
                .id(randomUUID)
                .status(TransactionStatus.PENDING)
                .build();

        when(transactionRepositoryMock.findById(randomUUID)).thenReturn(Optional.of(mockTransaction));

        assertThrows(ForbiddenRequestException.class, () -> transactionServiceMock.setTransactionToComplete(randomUUID));
    }

}
