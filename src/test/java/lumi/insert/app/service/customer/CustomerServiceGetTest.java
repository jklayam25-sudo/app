package lumi.insert.app.service.customer;
 
import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.domain.Specification;

import lumi.insert.app.dto.request.CustomerGetByFilter;
import lumi.insert.app.dto.request.CustomerGetNameRequest;
import lumi.insert.app.dto.response.CustomerDetailResponse;
import lumi.insert.app.dto.response.CustomerNameResponse;
import lumi.insert.app.dto.response.CustomerResponse;
import lumi.insert.app.entity.Customer;
import lumi.insert.app.exception.NotFoundEntityException;

public class CustomerServiceGetTest extends BaseCustomerServiceTest{
    
    @Test
    @DisplayName("Should return customer detail DTO when get customer found")
    void getCustomer_foundEntity_returnCustomerDTO(){
        when(customerRepository.findById(setupCustomer.getId())).thenReturn(Optional.of(setupCustomer));

        CustomerDetailResponse customer = customerServiceMock.getCustomer(setupCustomer.getId());
        assertEquals(setupCustomer.getName(), customer.name());
    }

    @Test
    @DisplayName("Should return customer detail DTO when get customer found")
    void getCustomer_notFoundEntity_throwNotFound(){
        when(customerRepository.findById(setupCustomer.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> customerServiceMock.getCustomer(setupCustomer.getId())); 
    }

    @Test
    @DisplayName("Should return customer Name DTO when found")
    void searchProductNames_foundEntity_returnCustomerNameDTO(){
        CustomerNameResponse name = new CustomerNameResponse(setupCustomer.getId(), setupCustomer.getName());

        Slice<CustomerNameResponse> slices = new SliceImpl<>(List.of(name));
        when(customerRepository.getByNameContainingIgnoreCase(eq("tes"), any(Pageable.class))).thenReturn(slices);

        CustomerGetNameRequest request = CustomerGetNameRequest.builder()
        .name("tes")
        .build();

        Slice<CustomerNameResponse> customer = customerServiceMock.searchCustomerNames(request);
        assertEquals(1, customer.getNumberOfElements());
        assertEquals(setupCustomer.getName(), customer.getContent().getFirst().name());
        verify(customerRepository, times(1)).getByNameContainingIgnoreCase(any(), argThat(arg -> arg.getPageSize() == 10));
    }

    @Test
    @DisplayName("Should return empty customer Name DTO when found")
    void searchProductNames_notFoundEntity_returnEmptyCustomerNameDTO(){ 

        Slice<CustomerNameResponse> slices = new SliceImpl<>(List.of());
        when(customerRepository.getByNameContainingIgnoreCase(eq("tes"), any(Pageable.class))).thenReturn(slices);

        CustomerGetNameRequest request = CustomerGetNameRequest.builder()
        .name("tes")
        .build();

        Slice<CustomerNameResponse> customer = customerServiceMock.searchCustomerNames(request);
        assertEquals(0, customer.getNumberOfElements());
        assertEquals(List.of(), customer.getContent());
        verify(customerRepository, times(1)).getByNameContainingIgnoreCase(any(), argThat(arg -> arg.getPageSize() == 10));
    }

    @Test
    @DisplayName("Should return Slice customer DTO when get customer found")
    void getCustomers_foundEntity_returnSliceCustomerDTO(){
        Page<Customer> slices = new PageImpl<>(List.of(setupCustomer));
        when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(slices);

        CustomerGetByFilter request = CustomerGetByFilter.builder()
        .isActive(false)
        .minTotalUnpaid(1000L)
        .maxTotalUnpaid(1500L)
        .build();

        Slice<CustomerResponse> customer = customerServiceMock.getCustomers(request);
        assertEquals(1, customer.getNumberOfElements()); 
    }

    @Test
    @DisplayName("Should return Slice customer DTO when get customer found")
    void getCustomers_notFoundEntity_returnSliceCustomerDTO(){
        Page<Customer> slices = new PageImpl<>(List.of());
        when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(slices);

        CustomerGetByFilter request = CustomerGetByFilter.builder()
        .isActive(false)
        .minTotalUnpaid(1000L)
        .maxTotalUnpaid(1500L)
        .build();

        Slice<CustomerResponse> customer = customerServiceMock.getCustomers(request);
        assertEquals(0, customer.getNumberOfElements()); 
    }
}
