package lumi.insert.app.service.transactionitem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows; 
import static org.mockito.ArgumentMatchers.any; 
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; 
import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.entity.nondatabase.TransactionStatus;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;

public class TransactionItemServiceEditTest extends BaseTransactionItemServiceTest {
    
    @Test
    @DisplayName("Should calcute Transaction total and update entity, return TransactionItemResponse DTO when updating transaction item is successful")
    public void updateTransactionItemQuantity_validRequest_returnTransactionItemResponse(){  
        setupTransaction.setTotalItems(1L);
        setupTransaction.setSubTotal(100000L);
        setupTransaction.setStatus(TransactionStatus.PENDING);
        setupTransactionItem.setTransaction(setupTransaction);

        setupProduct.setSellPrice(55000L);
        setupProduct.setStockQuantity(3L);
        setupTransactionItem.setProduct(setupProduct);
        
        setupTransactionItem.setPrice(50000L);
        setupTransactionItem.setQuantity(1L);

        when(transactionItemRepositoryMock.findById(setupTransactionItem.getId())).thenReturn(Optional.of(setupTransactionItem));

        TransactionItemResponse updateTransactionItem = transactionItemServiceMock.updateTransactionItemQuantity(setupTransactionItem.getId(), 2L);

        assertEquals(setupTransactionItem.getId(), updateTransactionItem.id());  
        assertEquals(160000L, setupTransaction.getGrandTotal());
        assertEquals(2L, setupTransactionItem.getQuantity());
        assertEquals(55000L, setupTransactionItem.getPrice());
    }

    @Test
    @DisplayName("Should thrown not found error when transaction item not found")
    public void updateTransactionItemQuantity_invalidId_throwNotFoundError(){ 
        when(transactionItemRepositoryMock.findById(any())).thenReturn(Optional.empty()); 

        assertThrows(NotFoundEntityException.class, () -> transactionItemServiceMock.deleteTransactionItem(null));
    }

    @Test
    @DisplayName("Should thrown forbidden request error when transaction status is not PENDING")
    public void updateTransactionItemQuantity_notPending_throwForbiddenReqError(){ 
        setupTransaction.setStatus(TransactionStatus.CANCELLED);
        setupTransactionItem.setTransaction(setupTransaction);

        when(transactionItemRepositoryMock.findById(any())).thenReturn(Optional.of(setupTransactionItem)); 

        assertThrows(ForbiddenRequestException.class, () -> transactionItemServiceMock.deleteTransactionItem(null));
    }

    @Test
    @DisplayName("Should thrown transactionValidate error when product stock lesser than request buy stock")
    public void updateTransactionItemQuantity_outOfStock_throwTransactionValidationExc(){  
        setupTransaction.setTotalItems(1L);
        setupTransaction.setSubTotal(100000L);
        setupTransaction.setStatus(TransactionStatus.PENDING);
        setupTransactionItem.setTransaction(setupTransaction);

        setupProduct.setSellPrice(55000L);
        setupProduct.setStockQuantity(3L);
        setupTransactionItem.setProduct(setupProduct);
        
        setupTransactionItem.setPrice(50000L);
        setupTransactionItem.setQuantity(1L);

        when(transactionItemRepositoryMock.findById(setupTransactionItem.getId())).thenReturn(Optional.of(setupTransactionItem));

         assertThrows(TransactionValidationException.class, () -> transactionItemServiceMock.updateTransactionItemQuantity(setupTransactionItem.getId(), 999L));

    }

}
