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
import lumi.insert.app.entity.TransactionStatus;
import lumi.insert.app.exception.ForbiddenRequestException;
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

    @Test
    @DisplayName("Should calcute Transaction total , return TransactionItemResponse DTO when refund action transaction item is successful")
    public void refundTransactionItem_validRequest_returnTransactionItemResponse(){
        setupProduct.setSellPrice(1050L);
        setupProduct.setStockQuantity(1L);

        setupTransaction.setStatus(TransactionStatus.COMPLETE);
        setupTransaction.setTotalUnpaid(3000L);

        setupTransactionItem.setPrice(1000L);
        setupTransactionItem.setQuantity(3L);
        setupTransactionItem.setTransaction(setupTransaction);
        setupTransactionItem.setProduct(setupProduct); 
        when(transactionItemRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransactionItem));
        when(transactionItemRepositoryMock.save(any())).thenAnswer(res -> res.getArgument(0));
        TransactionItemResponse transactionItem = transactionItemServiceMock.refundTransactionItem(setupTransactionItem.getId(), 2L);

        assertEquals(1000L, transactionItem.price());
        assertEquals(-2L, transactionItem.quantity()); 
        assertEquals(3L, setupProduct.getStockQuantity());
        assertEquals(1000L, setupTransaction.getTotalUnpaid());
        assertEquals(TransactionStatus.COMPLETE, setupTransaction.getStatus());
        assertEquals("REFUND: " + setupProduct.getName(), transactionItem.description());
    }
    
    @Test
    @DisplayName("Should thrown not found error when transactionItem not found")
    public void refundTransactionItem_invalidTransactionId_throwNotFoundError(){ 
        when(transactionItemRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> transactionItemServiceMock.refundTransactionItem(null, null));
    }

    @Test
    @DisplayName("Should thrown forbidden request error when refund quantity higher than actual")
    public void refundTransactionItem_refundMoreThanBought_throwForbiddenReqError(){ 
        setupTransactionItem.setQuantity(2L);
        when(transactionItemRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransactionItem));

        assertThrows(ForbiddenRequestException.class, () -> transactionItemServiceMock.refundTransactionItem(null, 3L));
    }
    
    @Test
    @DisplayName("Should thrown forbidden request error when refund an already refunded item (minus)")
    public void refundTransactionItem_refundAnRefunded_throwForbiddenReqError(){ 
        setupTransactionItem.setQuantity(-2L);
        when(transactionItemRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransactionItem));

        assertThrows(ForbiddenRequestException.class, () -> transactionItemServiceMock.refundTransactionItem(null, 3L));
    }   

    @Test
    @DisplayName("Should thrown forbidden request error when transaction status other than process / complete ")
    public void refundTransactionItem_statusPending_throwForbiddenReqError(){ 
        setupTransaction.setStatus(TransactionStatus.PENDING);
        setupTransactionItem.setQuantity(3L);
        setupTransactionItem.setTransaction(setupTransaction);
        when(transactionItemRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransactionItem));

        assertThrows(ForbiddenRequestException.class, () -> transactionItemServiceMock.refundTransactionItem(null, 3L));
    }   

}
