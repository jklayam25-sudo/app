package lumi.insert.app.controller.transactionitem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.exception.NotFoundEntityException;

public class TransactionItemControllerGetTest  extends BaseTransactionItemControllerTest{
    
    @Test
    @DisplayName("should return slice of Transaction item Response when request Trx item found")
    public void getTransactionItemsAPI_validId_shouldReturnEntity() throws Exception{
        Slice<TransactionItemResponse> slice = new SliceImpl<>(List.of(transactionItemResponse));
        when(transactionItemService.getTransactionItemsByTransactionId(any(UUID.class), any(PaginationRequest.class))).thenReturn(slice);

        mockMvc.perform(
            get("/api/transactions/" + UUID.randomUUID().toString() + "/items")
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.content[0].id").value(transactionItemResponse.id().toString()))
        .andExpect(jsonPath("$.data.content[0].transactionId").value(transactionItemResponse.transactionId().toString()))
        .andExpect(jsonPath("$.data.content.length()").value(1))
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("should return empty content when request Trx item not found")
    public void getTransactionItemsAPI_invalidId_shouldReturnErrorNotFound() throws Exception{
        Slice<TransactionItemResponse> slice = new SliceImpl<>(List.of());
        when(transactionItemService.getTransactionItemsByTransactionId(any(UUID.class), any(PaginationRequest.class))).thenReturn(slice);

        mockMvc.perform(
            get("/api/transactions/" + UUID.randomUUID().toString() + "/items")
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.content").isEmpty())
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content.length()").value(0))
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("should return errors missMatch when request Trx item id is not UUID")
    public void getTransactionItemsAPI_missmatchId_shouldReturnErrorMethodArgs() throws Exception{ 

        mockMvc.perform(
            get("/api/transactions/" + true + "/items") 
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.data").isEmpty()) 
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }
    
    @Test
    @DisplayName("should return Transaction item Response when request Trx item found")
    public void getTransactionItemByIdAPI_validId_shouldReturnEntity() throws Exception{
        when(transactionItemService.getTransactionItem(any(UUID.class))).thenReturn(transactionItemResponse);

        mockMvc.perform(
            get("/api/transactions/" + UUID.randomUUID().toString() + "/items/" + transactionItemResponse.id().toString())
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(transactionItemResponse.id().toString()))
        .andExpect(jsonPath("$.data.transactionId").value(transactionItemResponse.transactionId().toString()))
        .andExpect(jsonPath("$.data.productId").value(transactionItemResponse.productId()))
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("should return errors notFound when request Trx item not found")
    public void getTransactionItemByIdAPI_invalidId_shouldReturnErrorNotFound() throws Exception{
        when(transactionItemService.getTransactionItem(any(UUID.class))).thenThrow(new NotFoundEntityException("Transaction Items with ID " + 231 + " was not found"));

        mockMvc.perform(
            get("/api/transactions/" + UUID.randomUUID().toString() + "/items/" + transactionItemResponse.id().toString())
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.data").isEmpty()) 
        .andExpect(jsonPath("$.errors").value("Transaction Items with ID " + 231 + " was not found"));
    }

    @Test
    @DisplayName("should return errors missMatch when request Trx item id is not UUID")
    public void getTransactionItemByIdAPI_missmatchId_shouldReturnErrorMethodArgs() throws Exception{ 

        mockMvc.perform(
            get("/api/transactions/" + UUID.randomUUID().toString() + "/items/" + true) 
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.data").isEmpty()) 
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("should return Transaction item Response when request Trx item found")
    public void getTransactionItemByTrxIdProIdAPI_validId_shouldReturnEntity() throws Exception{
        when(transactionItemService.getTransactionByTransactionIdAndProductId(any(UUID.class), any(Long.class))).thenReturn(transactionItemResponse);

        mockMvc.perform(
            get("/api/transactions/" + UUID.randomUUID().toString() + "/items/product/" + 1L)
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(transactionItemResponse.id().toString()))
        .andExpect(jsonPath("$.data.transactionId").value(transactionItemResponse.transactionId().toString()))
        .andExpect(jsonPath("$.data.productId").value(transactionItemResponse.productId()))
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("should return error notFound when request Trx item not found")
    public void getTransactionItemByTrxIdProIdAPI_invalidProductId_shouldReturnErrorNotFound() throws Exception{
        when(transactionItemService.getTransactionByTransactionIdAndProductId(any(UUID.class), any(Long.class))).thenThrow(new NotFoundEntityException("Transaction Items was not found"));

        mockMvc.perform(
            get("/api/transactions/" + UUID.randomUUID().toString() + "/items/product/" + 1L)
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.data").isEmpty())
        .andExpect(jsonPath("$.errors").value("Transaction Items was not found"));
    }

    @Test
    @DisplayName("should return error missmatch when request Trx item not found")
    public void getTransactionItemByTrxIdProIdAPI_invalidProductId_shouldReturnErrorMissMatch() throws Exception{ 
        mockMvc.perform(
            get("/api/transactions/" + UUID.randomUUID().toString() + "/items/product/" + true)
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.data").isEmpty())
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

}
