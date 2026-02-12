package lumi.insert.app.controller.transactionitem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import lumi.insert.app.exception.NotFoundEntityException;

public class TransactionItemControllerGetTest  extends BaseTransactionItemControllerTest{
    
    @Test
    @DisplayName("should return Transaction item Response when request Trx item found")
    public void getTransactionItemAPI_validId_shouldReturnEntity() throws Exception{
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
    public void getTransactionItemAPI_invalidId_shouldReturnErrorNotFound() throws Exception{
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
    public void getTransactionItemAPI_missmatchId_shouldReturnErrorMethodArgs() throws Exception{ 

        mockMvc.perform(
            get("/api/transactions/" + UUID.randomUUID().toString() + "/items/" + true) 
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.data").isEmpty()) 
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

}
