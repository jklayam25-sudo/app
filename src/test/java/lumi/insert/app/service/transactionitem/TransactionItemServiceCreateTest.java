package lumi.insert.app.service.transactionitem;

import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.f4b6a3.uuid.UuidCreator;

import lumi.insert.app.dto.request.ItemRefundRequest;
import lumi.insert.app.dto.request.TransactionItemCreateRequest;
import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.entity.nondatabase.StockMove;
import lumi.insert.app.entity.nondatabase.TransactionStatus;
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
        setupCustomer.setTotalUnpaid(2000L);
        setupTransaction.setCustomer(setupCustomer);

        setupTransactionItem.setPrice(1000L);
        setupTransactionItem.setQuantity(3L);
        setupTransactionItem.setTransaction(setupTransaction);
        setupTransactionItem.setProduct(setupProduct); 
        when(transactionItemRepositoryMock.findByTransactionIdAndProductId(any(), anyLong())).thenReturn(List.of(setupTransactionItem));
        when(transactionItemRepositoryMock.save(any())).thenAnswer(res -> res.getArgument(0));
        when(stockCardRepositoryMock.save(any())).thenAnswer(res -> res.getArgument(0));

        ItemRefundRequest request = ItemRefundRequest.builder()
        .productId(setupProduct.getId())
        .quantity(2L)
        .build();
        
        TransactionItemResponse transactionItem = transactionItemServiceMock.refundTransactionItem(setupTransactionItem.getId(), request);

        assertEquals(1000L, transactionItem.price());
        assertEquals(-2L, transactionItem.quantity()); 
        assertEquals(3L, setupProduct.getStockQuantity());
        assertEquals(1000L, setupTransaction.getTotalUnpaid());
        assertEquals(0L, setupCustomer.getTotalUnpaid());
        assertEquals(TransactionStatus.COMPLETE, setupTransaction.getStatus());
        assertEquals("REFUND: " + setupProduct.getName(), transactionItem.description());

        verify(stockCardRepositoryMock, times(1)).save(argThat(arg -> arg.getOldStock() == 1L && arg.getQuantity() == 2L && arg.getNewStock() == 3L && arg.getType() == StockMove.CUSTOMER_IN));
    }

    @Test
    @DisplayName("Should calcute Transaction total , return TransactionItemResponse DTO when refund action transaction item is successful CASE 2: PARTIAL REFUND, BOUGHT 3Q, REFUND 1Q")
    public void refundTransactionItem_validRequestCase2_returnTransactionItemResponse(){
        
        setupProduct.setSellPrice(1000L);
        setupProduct.setStockQuantity(1L);

        setupTransaction.setStatus(TransactionStatus.PROCESS);
        setupTransaction.setTotalUnpaid(700L);
        setupTransaction.setTotalPaid(1300L);

        setupCustomer.setTotalUnpaid(1200L);
        setupCustomer.setTotalPaid(1300L);
        setupTransaction.setCustomer(setupCustomer);

        setupTransactionItem.setPrice(1000L);
        setupTransactionItem.setQuantity(3L);
        setupTransactionItem.setTransaction(setupTransaction);
        setupTransactionItem.setProduct(setupProduct); 

        TransactionItem partialRefund = TransactionItem.builder()
        .quantity(-1L)
        .build();

        when(transactionItemRepositoryMock.findByTransactionIdAndProductId(any(), anyLong())).thenReturn(List.of(setupTransactionItem, partialRefund));
        when(transactionItemRepositoryMock.save(any())).thenAnswer(res -> res.getArgument(0));
        when(stockCardRepositoryMock.save(any())).thenAnswer(res -> res.getArgument(0));

        ItemRefundRequest request = ItemRefundRequest.builder()
        .productId(setupProduct.getId())
        .quantity(1L)
        .build();
        
        TransactionItemResponse transactionItem = transactionItemServiceMock.refundTransactionItem(setupTransactionItem.getId(), request);
 
        assertEquals(-1L, transactionItem.quantity()); 
        assertEquals(2L, setupProduct.getStockQuantity());
        assertEquals(0L, setupTransaction.getTotalUnpaid());
        assertEquals(300L, setupTransaction.getTotalUnrefunded());
        assertEquals(500L, setupCustomer.getTotalUnpaid());
        assertEquals(1000L, setupCustomer.getTotalPaid());
        assertEquals(300L, setupCustomer.getTotalUnrefunded()); 
        assertEquals("REFUND: " + setupProduct.getName(), transactionItem.description());

        verify(stockCardRepositoryMock, times(1)).save(argThat(arg -> arg.getOldStock() == 1L && arg.getQuantity() == 1L && arg.getNewStock() == 2L && arg.getType() == StockMove.CUSTOMER_IN));
    }
    
    @Test
    @DisplayName("Should thrown not found error when transactionItem not found")
    public void refundTransactionItem_invalidTransactionId_throwNotFoundError(){ 
        when(transactionItemRepositoryMock.findByTransactionIdAndProductId(any(), anyLong())).thenReturn(List.of());

        assertThrows(NotFoundEntityException.class, () -> transactionItemServiceMock.refundTransactionItem(UuidCreator.getTimeOrderedEpochFast(), ItemRefundRequest.builder().productId(1L).build()));
    }

    @Test
    @DisplayName("Should thrown forbidden request error when refund quantity higher than actual")
    public void refundTransactionItem_refundMoreThanBought_throwForbiddenReqError(){ 
        setupTransactionItem.setQuantity(2L);
        when(transactionItemRepositoryMock.findByTransactionIdAndProductId(any(), anyLong())).thenReturn(List.of(setupTransactionItem));

        ItemRefundRequest request = ItemRefundRequest.builder()
        .productId(setupProduct.getId())
        .quantity(3L)
        .build();

        assertThrows(ForbiddenRequestException.class, () -> transactionItemServiceMock.refundTransactionItem(null, request));
    }

    @Test
    @DisplayName("Should thrown forbidden request error when refund quantity higher than actual CASE 2")
    public void refundTransactionItem_refundMoreThanLeft_throwForbiddenReqError(){ 
        setupTransactionItem.setQuantity(2L);

        TransactionItem transactionItem = TransactionItem.builder()
        .quantity(-2L)
        .build();

        when(transactionItemRepositoryMock.findByTransactionIdAndProductId(any(), anyLong())).thenReturn(List.of(setupTransactionItem, transactionItem));

        ItemRefundRequest request = ItemRefundRequest.builder()
        .productId(setupProduct.getId())
        .quantity(2L)
        .build();

        assertThrows(ForbiddenRequestException.class, () -> transactionItemServiceMock.refundTransactionItem(null, request));
    }
 
    @Test
    @DisplayName("Should thrown forbidden request error when transaction status other than process / complete ")
    public void refundTransactionItem_statusPending_throwForbiddenReqError(){ 
        setupTransaction.setStatus(TransactionStatus.PENDING);
        setupTransactionItem.setQuantity(3L);
        setupTransactionItem.setTransaction(setupTransaction);
        when(transactionItemRepositoryMock.findByTransactionIdAndProductId(any(), anyLong())).thenReturn(List.of(setupTransactionItem));

        ItemRefundRequest request = ItemRefundRequest.builder()
        .productId(setupProduct.getId())
        .quantity(3L)
        .build();
        
        assertThrows(ForbiddenRequestException.class, () -> transactionItemServiceMock.refundTransactionItem(null, request));
    }   

}
