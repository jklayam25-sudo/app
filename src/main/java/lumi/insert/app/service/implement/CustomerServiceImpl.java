package lumi.insert.app.service.implement;
 
import java.util.UUID; 

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.request.CustomerCreateRequest;
import lumi.insert.app.dto.request.CustomerGetByFilter;
import lumi.insert.app.dto.request.CustomerGetNameRequest;
import lumi.insert.app.dto.request.CustomerUpdateRequest;
import lumi.insert.app.dto.response.CustomerDetailResponse;
import lumi.insert.app.dto.response.CustomerNameResponse;
import lumi.insert.app.dto.response.CustomerResponse;
import lumi.insert.app.entity.Customer;
import lumi.insert.app.exception.DuplicateEntityException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.repository.CustomerRepository;
import lumi.insert.app.service.CustomerService;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.CustomerMapper;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    JpaSpecGenerator jpaSpec;

    @Override
    public CustomerDetailResponse createCustomer(CustomerCreateRequest request) {
        if(customerRepository.existsByName(request.getName())) throw new DuplicateEntityException("Customer with name " + request.getName() + " already exists");

        Customer customer = Customer.builder()
        .name(request.getName())
        .email(request.getEmail())
        .contact(request.getContact())
        .shippingAddress(request.getShippingAddress())
        .build();

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.createDtoDetailResponseFromEmployee(savedCustomer);
    }

    @Override
    public CustomerDetailResponse getCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Customer with id " + id + " is not found"));

        return customerMapper.createDtoDetailResponseFromEmployee(customer);
    }

    @Override
    public Slice<CustomerResponse> getCustomers(CustomerGetByFilter request) {
        Pageable pageable = jpaSpec.pageable(request);

        Specification<Customer> customerSpecification = jpaSpec.customerSpecification(request);

        Slice<Customer> customers = customerRepository.findAll(customerSpecification, pageable);
        return customers.map(customerMapper::createDtoResponseFromEmployee);
    }

    @Override
    public Slice<CustomerNameResponse> searchCustomerNames(CustomerGetNameRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()).withSort(Sort.by("name").descending());
        return customerRepository.getByNameContainingIgnoreCase(request.getName(), pageable);
    }

    @Override
    public CustomerDetailResponse updateCustomer(UUID id, CustomerUpdateRequest request) {
        if(request.getName() != null && customerRepository.existsByName(request.getName())) throw new DuplicateEntityException("Customer with name " + request.getName() + " already exists");

        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Customer with id " + id + " is not found"));

        customerMapper.updateEntityFromDto(request, customer);

        return customerMapper.createDtoDetailResponseFromEmployee(customer);
    }
    
}
