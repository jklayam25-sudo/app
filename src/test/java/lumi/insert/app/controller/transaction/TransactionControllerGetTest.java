package lumi.insert.app.controller.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
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

import lumi.insert.app.dto.request.TransactionGetByFilter;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.TransactionStatus;
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
    @DisplayName("should return errors of invalid method when request Trx id is not UUID")
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

    @Test
    @DisplayName("should return Slice of Transaction Response")
    public void getTransactionsAPI_filterRequest_shouldReturnSliceOfEntity() throws Exception{
        Slice<TransactionResponse> slice = new SliceImpl<>(List.of(transactionResponse));
        when(transactionService.searchTransactionsByRequests(any(TransactionGetByFilter.class))).thenReturn(slice);

        mockMvc.perform(
            get("/api/transactions/filter?status=PENDING")
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.content.length()").value(1)) 
        .andExpect(jsonPath("$.data.content[0].id").value(transactionResponse.id().toString())) 
        .andExpect(jsonPath("$.errors").isEmpty());

        verify(transactionService).searchTransactionsByRequests(argThat(req -> 
        req.getStatus() == TransactionStatus.PENDING &&
        req.getMinCreatedAt() == null
        ));
    }

    @Test
    @DisplayName("should return Slice < Empty of Transaction Response")
    public void getTransactionsAPI_emptyData_shouldReturnSliceOfEntity() throws Exception{
        Slice<TransactionResponse> slice = new SliceImpl<>(List.of());
        when(transactionService.searchTransactionsByRequests(any(TransactionGetByFilter.class))).thenReturn(slice);

        mockMvc.perform(
            get("/api/transactions/filter?status=PENDING")
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.content.length()").value(0)) 
        .andExpect(jsonPath("$.data.content").isEmpty()) 
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("should return errors of missmatch when request parameter invalid")
    public void getTransactionsAPI_emptyData_shouldReturnErrorsMissMatch() throws Exception{
        Slice<TransactionResponse> slice = new SliceImpl<>(List.of());
        when(transactionService.searchTransactionsByRequests(any(TransactionGetByFilter.class))).thenReturn(slice);

        mockMvc.perform(
            get("/api/transactions/filter?minCreatedAt=" + true)
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest()) 
        .andExpect(jsonPath("$.data").isEmpty()) 
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("should return errors of min constraint when request parameter invalid")
    public void getTransactionsAPI_minusParamFilter_shouldReturnErrorsMissMatch() throws Exception{
        Slice<TransactionResponse> slice = new SliceImpl<>(List.of());
        when(transactionService.searchTransactionsByRequests(any(TransactionGetByFilter.class))).thenReturn(slice);

        mockMvc.perform(
            get("/api/transactions/filter?minTotalItems=" + -2)
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest()) 
        .andExpect(jsonPath("$.data").isEmpty()) 
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("should return errors of pattern constraint when request parameter invalid")
    public void getTransactionsAPI_sortBySpec_shouldReturnErrorsMissMatch() throws Exception{
        Slice<TransactionResponse> slice = new SliceImpl<>(List.of());
        when(transactionService.searchTransactionsByRequests(any(TransactionGetByFilter.class))).thenReturn(slice);

        mockMvc.perform(
            get("/api/transactions/filter?sortBy=" + -2)
            .accept(MediaType.APPLICATION_JSON_VALUE) 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest()) 
        .andExpect(jsonPath("$.data").isEmpty()) 
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }
}
