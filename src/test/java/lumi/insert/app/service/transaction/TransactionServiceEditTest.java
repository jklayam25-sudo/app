package lumi.insert.app.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq; 
import static org.mockito.Mockito.when;
 
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lumi.insert.app.exception.BoilerplateRequestException;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.repository.projection.ProductRefreshProjection;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.nondatabase.TransactionStatus;

public class TransactionServiceEditTest extends BaseTransactionServiceTest{

    @Test
    @DisplayName("Should throw NotFoundEntityException when id is invalid")
    public void setTransactionToProcess_invalidId_throwNotFound(){
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, ()-> transactionServiceMock.setTransactionToProcess(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should throw ForbiddenRequestException when trx status isn't pending > setToProcess only from pending")
    public void setTransactionToProcess_statusNonPending_throwNotFound(){
        setupTransaction.setStatus(TransactionStatus.COMPLETE);
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransaction));

        assertThrows(ForbiddenRequestException.class, ()-> transactionServiceMock.setTransactionToProcess(UUID.randomUUID()));
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

    @Test
    @DisplayName("Should throw ForbiddenRequestException when trx status isn't pending > setToProcess only from pending")
    public void cancelTransaction_statusNonPending_throwNotFound(){
        setupTransaction.setStatus(TransactionStatus.CANCELLED);
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransaction));

        assertThrows(ForbiddenRequestException.class, ()-> transactionServiceMock.cancelTransaction(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should throw NotFoundEntityException when id is invalid for Complete")
    public void cancelTransaction_invalidId_throwNotFound() {
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> transactionServiceMock.cancelTransaction(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should cancel and add product stock when cancelTrx complete")
    public void cancelTransaction_validRequest_returnDtoAndReverseProduct() {
        setupProduct.setStockQuantity(1L);
        setupProduct.setSellPrice(1000L);

        setupTransactionItem.setProduct(setupProduct);
        setupTransactionItem.setQuantity(4L);
        setupTransactionItem.setPrice(setupProduct.getSellPrice());

        setupTransaction.setTotalPaid(4000L);
        setupTransaction.getTransactionItems().add(setupTransactionItem);
        setupTransaction.setStatus(TransactionStatus.COMPLETE);

        when(productRepositoryMock.findAllById(List.of(1L))).thenReturn(List.of(setupProduct));
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransaction));

        TransactionResponse cancelTransaction = transactionServiceMock.cancelTransaction(UUID.randomUUID());
        assertEquals(TransactionStatus.CANCELLED, cancelTransaction.status());
        assertEquals(0L, cancelTransaction.totalPaid());
        assertEquals(4000L, cancelTransaction.totalUnrefunded());
    }

    @Test
    @DisplayName("Should refresh and add calculate base on newer update")
    public void refreshTransaction_validRequest_returnDtoAndRefreshTransaction() {
        setupProduct.setStockQuantity(3L);
        setupProduct.setSellPrice(1000L);

        setupTransactionItem.setProduct(setupProduct);
        setupTransactionItem.setQuantity(2L);
        setupTransactionItem.setPrice(setupProduct.getSellPrice() - 200);

        setupTransaction.setGrandTotal(1600L);
        setupTransaction.getTransactionItems().add(setupTransactionItem);
        setupTransaction.setStatus(TransactionStatus.PENDING);

        ProductRefreshProjection productRefreshProjection = new ProductRefreshProjection(setupProduct.getId(), setupProduct.getSellPrice(), setupProduct.getStockQuantity());
        when(productRepositoryMock.searchIdUpdatedAtMoreThan(eq(List.of(1L)), any())).thenReturn(List.of(productRefreshProjection));
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransaction));

        TransactionResponse refreshTransaction = transactionServiceMock.refreshTransaction(UUID.randomUUID()); 
        assertEquals(2000L, refreshTransaction.grandTotal()); 
    }

    @Test
    @DisplayName("Should refresh and add calculate base on newer update CASE: STOCK 0")
    public void refreshTransaction_outOfStock_returnDtoAndRefreshTransaction() {
        setupProduct.setStockQuantity(0L);
        setupProduct.setSellPrice(1000L);

        setupTransactionItem.setProduct(setupProduct);
        setupTransactionItem.setQuantity(2L);
        setupTransactionItem.setPrice(setupProduct.getSellPrice() - 200);

        setupTransaction.setGrandTotal(1600L);
        setupTransaction.getTransactionItems().add(setupTransactionItem);
        setupTransaction.setStatus(TransactionStatus.PENDING);

        ProductRefreshProjection productRefreshProjection = new ProductRefreshProjection(setupProduct.getId(), setupProduct.getSellPrice(), setupProduct.getStockQuantity());
        when(productRepositoryMock.searchIdUpdatedAtMoreThan(eq(List.of(1L)), any())).thenReturn(List.of(productRefreshProjection));
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransaction));

        TransactionResponse refreshTransaction = transactionServiceMock.refreshTransaction(UUID.randomUUID()); 
        assertEquals(0L, refreshTransaction.grandTotal()); 
        assertEquals("Product stock lesser than " + 2 + ", transaction quantity decreased to 0", refreshTransaction.messages().getFirst()); 
    }

    @Test
    @DisplayName("Should throw ForbiddenRequestException when trx status isn't pending > setToProcess only from pending")
    public void refreshTransaction_statusNonPending_throwNotFound(){
        setupTransaction.setStatus(TransactionStatus.CANCELLED);
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransaction));

        assertThrows(ForbiddenRequestException.class, ()-> transactionServiceMock.refreshTransaction(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should throw NotFoundEntityException when id is invalid for Complete")
    public void refreshTransaction_invalidId_throwNotFound() {
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> transactionServiceMock.refreshTransaction(UUID.randomUUID()));
    }

}
