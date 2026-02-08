package lumi.insert.app.service.transactionitem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.exception.NotFoundEntityException;

public class TransactionItemServiceGetTest extends BaseTransactionItemServiceTest{
    @Test
    @DisplayName("Should return slice of TransactionItemResponce when transaction item found")
    public void getTransactionItemsByTransactionId_validId_returnSliceOfTransactionItem(){
        Slice<TransactionItem> slice = new SliceImpl<>(List.of(setupTransactionItem));

        when(transactionItemRepositoryMock.findAllByTransactionId(any(UUID.class), any(Pageable.class))).thenReturn(slice);

        PaginationRequest request = PaginationRequest.builder() 
        .build();
        
        Slice<TransactionItemResponse> transactionItemsByTransactionId = transactionItemServiceMock.getTransactionItemsByTransactionId(UUID.randomUUID(), request);  
        assertEquals(1, transactionItemsByTransactionId.getNumberOfElements());
        assertEquals(setupTransactionItem.getPrice(), transactionItemsByTransactionId.getContent().getFirst().price());                                                   
    }

    @Test
    @DisplayName("Should thrown not found error when transaction item not found")
    public void getTransactionByTransactionIdAndProductId_invalidId_throwNotFoundError(){ 
        when(transactionItemRepositoryMock.findByTransactionIdAndProductId(any(), any())).thenReturn(Optional.empty()); 

        assertThrows(NotFoundEntityException.class, () -> transactionItemServiceMock.getTransactionByTransactionIdAndProductId(null, null));
    }

    @Test
    @DisplayName("Should thrown not found error when transaction item not found")
    public void getTransactionByTransactionIdAndProductId_validId_throwNotFoundError(){ 
        when(transactionItemRepositoryMock.findByTransactionIdAndProductId(any(), any())).thenReturn(Optional.of(setupTransactionItem)); 

         TransactionItemResponse transactionByTransactionIdAndProductId = transactionItemServiceMock.getTransactionByTransactionIdAndProductId(null, null);

         assertEquals(setupTransactionItem.getId(), transactionByTransactionIdAndProductId.id());
    }
}
