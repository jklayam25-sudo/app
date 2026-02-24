package lumi.insert.app.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.dto.request.CustomerCreateRequest;
import lumi.insert.app.dto.request.CustomerGetByFilter;
import lumi.insert.app.dto.request.CustomerGetNameRequest;
import lumi.insert.app.dto.request.CustomerUpdateRequest; 
import lumi.insert.app.dto.response.CustomerDetailResponse;
import lumi.insert.app.dto.response.CustomerNameResponse;
import lumi.insert.app.dto.response.CustomerResponse; 
import lumi.insert.app.service.CustomerService;

@RestController
@Slf4j
public class CustomerController {
    
    @Autowired
    CustomerService customerService;

    @PostMapping(
        path = "/api/customers",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<CustomerDetailResponse>> createCustomerAPI(@Valid CustomerCreateRequest request){
        
        CustomerDetailResponse resultFromService = customerService.createCustomer(request);

        WebResponse<CustomerDetailResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);
    }

    @GetMapping(
        path = "/api/customers/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<CustomerDetailResponse>> getCustomerAPI(@PathVariable(name = "id") UUID id){
        CustomerDetailResponse resultFromService = customerService.getCustomer(id);

        WebResponse<CustomerDetailResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/customers",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<CustomerResponse>>> getCustomersAPI(@Valid @ModelAttribute CustomerGetByFilter request){ 
        Slice<CustomerResponse> resultFromService = customerService.getCustomers(request);

        WebResponse<Slice<CustomerResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/customers/searchName",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<CustomerNameResponse>>> searchCustomerNamesAPI(@Valid @ModelAttribute CustomerGetNameRequest request){
        Slice<CustomerNameResponse> resultFromService = customerService.searchCustomerNames(request);

        WebResponse<Slice<CustomerNameResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @PatchMapping(
        path = "/api/customers/{id}",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<CustomerDetailResponse>> updateCustomerAPI(@PathVariable(name = "id") UUID id, @Valid CustomerUpdateRequest request){ 
        CustomerDetailResponse resultFromService = customerService.updateCustomer(id, request);

        WebResponse<CustomerDetailResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }
}
