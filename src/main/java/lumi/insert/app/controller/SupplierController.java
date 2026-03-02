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
import lumi.insert.app.dto.request.SupplierCreateRequest;
import lumi.insert.app.dto.request.SupplierGetByFilter;
import lumi.insert.app.dto.request.SupplierGetNameRequest;
import lumi.insert.app.dto.request.SupplierUpdateRequest; 
import lumi.insert.app.dto.response.SupplierDetailResponse;
import lumi.insert.app.dto.response.SupplierNameResponse; 
import lumi.insert.app.service.SupplierService;

@RestController
@Slf4j
public class SupplierController {
    
    @Autowired
    SupplierService supplierService;

    @PostMapping(
        path = "/api/suppliers",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<SupplierDetailResponse>> createSupplierAPI(@Valid SupplierCreateRequest request){
        
        SupplierDetailResponse resultFromService = supplierService.createSupplier(request);

        WebResponse<SupplierDetailResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);
    }

    @GetMapping(
        path = "/api/suppliers/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<SupplierDetailResponse>> getSupplierAPI(@PathVariable(name = "id") UUID id){
        SupplierDetailResponse resultFromService = supplierService.getSupplier(id);

        WebResponse<SupplierDetailResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/suppliers",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<SupplierDetailResponse>>> getSuppliersAPI(@Valid @ModelAttribute SupplierGetByFilter request){ 
        Slice<SupplierDetailResponse> resultFromService = supplierService.getSuppliers(request);

        WebResponse<Slice<SupplierDetailResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/suppliers/searchName",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<SupplierNameResponse>>> searchSupplierNamesAPI(@Valid @ModelAttribute SupplierGetNameRequest request){
        Slice<SupplierNameResponse> resultFromService = supplierService.searchSupplierNames(request);

        WebResponse<Slice<SupplierNameResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @PatchMapping(
        path = "/api/suppliers/{id}",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<SupplierDetailResponse>> updateSupplierAPI(@PathVariable(name = "id") UUID id, @Valid SupplierUpdateRequest request){ 
        SupplierDetailResponse resultFromService = supplierService.updateSupplier(id, request);

        WebResponse<SupplierDetailResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }
}
