package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.request.CustomerGetByFilter;
import lumi.insert.app.dto.response.CustomerNameResponse;
import lumi.insert.app.entity.Customer;
import lumi.insert.app.utils.generator.JpaSpecGenerator; 

@DataJpaTest
@Transactional
@Import(JpaSpecGenerator.class)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    JpaSpecGenerator specGenerator;

    @Test
    @DisplayName("Should return saved entity when repository save success")
    void saveCustomer_validRequest_returnSaved(){
        Customer customer = Customer.builder()
        .name("TESTCUSTOMER")
        .email("TEST@mail.com")
        .contact("081234567890")
        .shippingAddress("Jl. xxx")
        .build();

        Customer saveAndFlush = customerRepository.saveAndFlush(customer);
        assertNotNull(saveAndFlush.getId());
        assertEquals("TEST@mail.com", saveAndFlush.getEmail());
        assertEquals(0, saveAndFlush.getTotalPaid());
    }

    @Test
    @DisplayName("Should return saved entity when repository save success")
    void existsCustomerName_validRequest_returnTrue(){
        Customer customer = Customer.builder()
        .name("TESTCUSTOMER")
        .email("TEST@mail.com")
        .contact("081234567890")
        .shippingAddress("Jl. xxx")
        .build();

         customerRepository.saveAndFlush(customer);
        assertTrue(customerRepository.existsByName(customer.getName()));  
    }

    @Test
    @DisplayName("Should return saved entity when repository save success")
    void getCustomerName_validRequest_returnSliceOfName(){
        Customer customer = Customer.builder()
        .name("TESTCUSTOMER")
        .email("TEST@mail.com")
        .contact("081234567890")
        .shippingAddress("Jl. xxx")
        .build();

        customerRepository.saveAndFlush(customer);
        
        Slice<CustomerNameResponse> names = customerRepository.getByNameContainingIgnoreCase("test", PageRequest.of(0, 5));;
        assertEquals(1, names.getNumberOfElements()); 
        assertTrue(names.isLast()); 
        assertEquals(customer.getName(), names.getContent().getFirst().name()); 
    }

    @Test
    @DisplayName("Should return filtered customer when found case 1: customer is inactive with total unpaid more than 1000 and less than 1500")
    void getCustomers_inactiveAndTotalUnpaid_shouldReturnDTO(){
        Customer matchCustomer = Customer.builder()
        .name("TESTCUSTOMER")
        .email("TEST@mail.com")
        .contact("081234567890")
        .shippingAddress("Jl. xxx")
        .isActive(false)
        .totalUnpaid(1200L)
        .build();

        Customer unMatchCustomer = Customer.builder()
        .name("TESTCUSTOMER3")
        .email("TEST@mail.com")
        .contact("081234567890")
        .shippingAddress("Jl. xxx")
        .isActive(false)
        .totalUnpaid(1600L)
        .build();

        Customer unMatchCustomer1 = Customer.builder()
        .name("TESTCUSTOMER2")
        .email("TEST@mail.com")
        .contact("081234567890")
        .shippingAddress("Jl. xxx")
        .totalUnpaid(1200L)
        .build();

        customerRepository.saveAllAndFlush(List.of(matchCustomer, unMatchCustomer, unMatchCustomer1));

        CustomerGetByFilter request = CustomerGetByFilter.builder()
        .isActive(false)
        .minTotalUnpaid(1000L)
        .maxTotalUnpaid(1500L)
        .build();

        Pageable pageable = specGenerator.pageable(request);

        Specification<Customer> customerSpecification = specGenerator.customerSpecification(request);

        Slice<Customer> customers = customerRepository.findAll(customerSpecification, pageable);
        assertEquals(1, customers.getNumberOfElements());
        assertEquals(matchCustomer.getName(), customers.getContent().getFirst().getName());
    }

    @Test
    @DisplayName("Should return filtered customer when found case 2: Empty Content")
    void getCustomers_emptyResult_shouldReturnEmptyArrDTO(){

        CustomerGetByFilter request = CustomerGetByFilter.builder()
        .isActive(false)
        .minTotalUnpaid(1000L)
        .maxTotalUnpaid(1500L)
        .build();

        Pageable pageable = specGenerator.pageable(request);

        Specification<Customer> customerSpecification = specGenerator.customerSpecification(request);

        Slice<Customer> customers = customerRepository.findAll(customerSpecification, pageable);
        assertEquals(0, customers.getNumberOfElements());
        assertEquals(List.of(), customers.getContent());
    }


}
