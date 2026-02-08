package lumi.insert.app.service.transactionitem;

import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import lumi.insert.app.dto.request.TransactionItemCreateRequest;
import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException; 

public class TransactionItemServiceCreateTest extends BaseTransactionItemServiceTest{
    
    @Test
    @DisplayName("Should calcute Transaction total , return TransactionItemResponse DTO when creating transaction item is successful")
    public void createTransactionItem_validRequest_returnTransactionItemResponse(){
        when(productRepositoryMock.findById(999L)).thenReturn(Optional.of(setupProduct));
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransaction));

        TransactionItemCreateRequest request = TransactionItemCreateRequest.builder()
         .productId(999L)
         .quantity(9L)
         .build();

        TransactionItemResponse transactionItem = transactionItemServiceMock.createTransactionItem(null, request);

        assertEquals(19000L, transactionItem.price());
        assertEquals(9L, transactionItem.quantity());
        assertEquals(999L, transactionItem.productId());
        assertEquals(10, setupProduct.getStockQuantity());
        assertEquals(19000L * 9L, setupTransaction.getGrandTotal());
        assertEquals(1L, setupTransaction.getTotalItems());
    }

    @Test
    @DisplayName("Should thrown not found error when transaction not found")
    public void createTransactionItem_invalidId_throwNotFoundError(){ 
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> transactionItemServiceMock.createTransactionItem(null, null));
    }

    @Test
    @DisplayName("Should thrown not found error when product not found")
    public void createTransactionItem_invalidProductId_throwNotFoundError(){ 
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.of(Transaction.builder().build()));
        when(productRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> transactionItemServiceMock.createTransactionItem(null, TransactionItemCreateRequest.builder().productId(1l).build()));
    }

    @Test
    @DisplayName("Should thrown transactionValidate error when product stock lesser than request buy stock")
    public void createTransactionItem_outOfStock_throwTransactionValidateError(){
        when(productRepositoryMock.findById(999L)).thenReturn(Optional.of(setupProduct));
        when(transactionRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransaction));

        TransactionItemCreateRequest request = TransactionItemCreateRequest.builder()
         .productId(999L)
         .quantity(19L)
         .build();

        assertThrows(TransactionValidationException.class, () -> transactionItemServiceMock.createTransactionItem(null, request));
    }

}
