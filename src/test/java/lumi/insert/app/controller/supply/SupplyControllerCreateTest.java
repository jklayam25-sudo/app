package lumi.insert.app.controller.supply;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import lumi.insert.app.dto.request.SupplyCreateRequest;
import lumi.insert.app.exception.NotFoundEntityException; 

public class SupplyControllerCreateTest extends BaseSupplyControllerTest{
    
    @Test
    @DisplayName("should return Supply Response when create succesfully")
    public void createSupplyAPI_validRequest_shouldReturnCreatedEntity() throws Exception{
        when(supplyService.createSupply(any(SupplyCreateRequest.class))).thenReturn(supplyResponse);
        mockMvc.perform(
            post("/api/supplies")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("supplierId", UUID.randomUUID().toString())
            .param("invoiceId","INV") 
            .param("totalFee","0") 
            .param("totalDiscount","0") 
            .param("supplyItems[0].productId","1") 
            .param("supplyItems[0].price","100") 
            .param("supplyItems[0].quantity","10") 
            
        )
        .andDo(print()) 
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.invoiceId").value(supplyResponse.invoiceId()))
        .andExpect(jsonPath("$.errors").isEmpty());

        verify(supplyService, times(1)).createSupply(argThat( arg -> arg.getSupplyItems().size() == 1 && arg.getSupplyItems().getFirst().getProductId() == 1L));
    }

    @Test
    @DisplayName("should return errors NotNull when request param is not valid")
    public void createSupplyAPI_nullCustomerId_shouldReturnNotNullError() throws Exception{ 
        mockMvc.perform(
            post("/api/supplies")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("supplierId", UUID.randomUUID().toString())
            .param("invoiceId","INV") 
            .param("totalFee","-10") 
            .param("totalDiscount","0") 
            .param("supplyItems[0].productId","1") 
            .param("supplyItems[0].price","100") 
            .param("supplyItems[0].quantity","10") 
            
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").value("totalFee cannot below 0"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return errors NotNull when request param is not valid")
    public void createSupplyAPI_emptyHeaderParam_shouldReturnNotNullError() throws Exception{ 
        mockMvc.perform(
            post("/api/supplies")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("supplierId", UUID.randomUUID().toString())
            .param("invoiceId","INV")  
            .param("totalDiscount","0") 
            .param("supplyItems[0].productId","1") 
            .param("supplyItems[0].price","100") 
            .param("supplyItems[0].quantity","10") 
            
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").value("totalFee cannot be empty"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return errors NotNull when request param is not valid")
    public void createSupplyAPI_emptyWhiteSpace_shouldReturnNotNullError() throws Exception{
        when(supplyService.createSupply(any(SupplyCreateRequest.class))).thenReturn(supplyResponse);
        mockMvc.perform(
            post("/api/supplies")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED) 
            .param("supplierId", " ") 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isNotEmpty())
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return errors NotNull when request param is not valid")
    public void createSupplyAPI_nullItems_shouldReturnNotNullError() throws Exception{ 
        mockMvc.perform(
            post("/api/supplies")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("supplierId", UUID.randomUUID().toString())
            .param("invoiceId","INV") 
            .param("totalFee","10") 
            .param("totalDiscount","0")  
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").value("items cannot be empty"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return Supply Response when create succesfully")
    public void createSupplyAPI_invalidParamOfItems_shouldReturnCreatedEntity() throws Exception{
        when(supplyService.createSupply(any(SupplyCreateRequest.class))).thenReturn(supplyResponse);
        mockMvc.perform(
            post("/api/supplies")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("supplierId", UUID.randomUUID().toString())
            .param("invoiceId","INV") 
            .param("totalFee","0") 
            .param("totalDiscount","0") 
            .param("supplyItems[0].productId","1") 
            .param("supplyItems[0].price","100")  
            
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").value("quantity cannot be empty"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return Supply Response when create succesfully")
    public void createSupplyAPI_minusProductQuantityAtItems_shouldReturnCreatedEntity() throws Exception{
        when(supplyService.createSupply(any(SupplyCreateRequest.class))).thenReturn(supplyResponse);
        mockMvc.perform(
            post("/api/supplies")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("supplierId", UUID.randomUUID().toString())
            .param("invoiceId","INV") 
            .param("totalFee","0") 
            .param("totalDiscount","0") 
            .param("supplyItems[0].productId","1") 
            .param("supplyItems[0].price","100")  
            .param("supplyItems[0].quantity","-100") 
        )
        .andDo(print()) 
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").value("quantity cannot below 0"))
        .andExpect(jsonPath("$.data").isEmpty());
    } 

    @Test
    @DisplayName("should return errors of notFoundEntity when request Trx id isn't valid")
    public void createSupplyAPI_invalidSupplierId_shouldReturnErrors() throws Exception{
        when(supplyService.createSupply(any(SupplyCreateRequest.class))).thenThrow(new NotFoundEntityException("Supplier with id is not found"));

        mockMvc.perform(
            post("/api/supplies")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("supplierId", UUID.randomUUID().toString())
            .param("invoiceId","INV") 
            .param("totalFee","0") 
            .param("totalDiscount","0") 
            .param("supplyItems[0].productId","1") 
            .param("supplyItems[0].price","100") 
            .param("supplyItems[0].quantity","10")   
        )
        .andDo(print()) 
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.data").isEmpty())
        .andExpect(jsonPath("$.errors").value("Supplier with id is not found")); 
    }
}
