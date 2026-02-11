package lumi.insert.app.controller.transaction;

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

public class TransactionControllerGetTest extends BaseTransactionControllerTest{
    
    @Test
    @DisplayName("should return Transaction Response when request Trx id is valid")
    public void getTransactionAPI_validId_shouldReturnEntity() throws Exception{
        when(transactionService.getTransaction(any(UUID.class))).thenReturn(transactionResponse);
        mockMvc.perform(
            get("/api/transactions/" + transactionResponse.id().toString())
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(transactionResponse.id().toString()))
        .andExpect(jsonPath("$.data.invoiceId").value(transactionResponse.invoiceId()))
        .andExpect(jsonPath("$.data.customerId").value(transactionResponse.customerId().toString()))
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("should return Transaction Response when request Trx id is invalid")
    public void getTransactionAPI_invalidId_shouldReturnErrorsOfNotFound() throws Exception{
        when(transactionService.getTransaction(any(UUID.class))).thenThrow(new NotFoundEntityException("Transaction with ID 1 was not found"));
        mockMvc.perform(
            get("/api/transactions/" + transactionResponse.id().toString())
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors").value("Transaction with ID 1 was not found")) 
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return Transaction Response when request Trx id is not UUID")
    public void getTransactionAPI_missMatch_shouldReturnErrors() throws Exception{ 
        mockMvc.perform(
            get("/api/transactions/" + true)
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isNotEmpty()) 
        .andExpect(jsonPath("$.data").isEmpty());
    }
}
