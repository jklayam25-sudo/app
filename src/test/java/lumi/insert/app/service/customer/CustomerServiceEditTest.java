package lumi.insert.app.service.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lumi.insert.app.dto.request.CustomerUpdateRequest;
import lumi.insert.app.dto.response.CustomerDetailResponse;
import lumi.insert.app.exception.DuplicateEntityException;
import lumi.insert.app.exception.NotFoundEntityException;

public class CustomerServiceEditTest extends BaseCustomerServiceTest{
    
    @Test
    @DisplayName("Should return new updated Customer when update success")
    void updateCustomer_validRequest_ReturnUpdatedDTO(){
        when(customerRepository.existsByName(anyString())).thenReturn(false);
        when(customerRepository.findById(setupCustomer.getId())).thenReturn(Optional.of(setupCustomer));

        CustomerUpdateRequest request = CustomerUpdateRequest.builder()
        .name("new Name LTE")
        .isActive(false)
        .build();

        CustomerDetailResponse updatedCustomer = customerServiceMock.updateCustomer(setupCustomer.getId(), request);
        assertEquals(request.getName(), updatedCustomer.name());
        assertEquals(setupCustomer.getId(), updatedCustomer.id());
        assertFalse( updatedCustomer.isActive());
    }

    @Test
    @DisplayName("Should thorw NotFoundEntity Exc when requested customer is not found")
    void updateCustomer_notFound_throwNotFound(){
        when(customerRepository.existsByName(anyString())).thenReturn(false);
        when(customerRepository.findById(setupCustomer.getId())).thenReturn(Optional.empty());

        CustomerUpdateRequest request = CustomerUpdateRequest.builder()
        .name("new Name LTE")
        .isActive(false)
        .build();

        assertThrows(NotFoundEntityException.class, () -> customerServiceMock.updateCustomer(setupCustomer.getId(), request)) ;
    }

    @Test
    @DisplayName("Should thorw DuplicateEntityException Exc when requested update name is exists")
    void updateCustomer_duplicateEntity_throwDuplicate(){
        when(customerRepository.existsByName("new Name LTE")).thenReturn(true);

        CustomerUpdateRequest request = CustomerUpdateRequest.builder()
        .name("new Name LTE")
        .isActive(false)
        .build();

        assertThrows(DuplicateEntityException.class, () -> customerServiceMock.updateCustomer(setupCustomer.getId(), request)) ;
    }
}
