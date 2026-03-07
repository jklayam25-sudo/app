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
import lumi.insert.app.dto.request.ItemRefundRequest;
import lumi.insert.app.dto.request.SupplyCreateRequest;
import lumi.insert.app.dto.request.SupplyGetByFilter; 
import lumi.insert.app.dto.request.SupplyUpdateRequest; 
import lumi.insert.app.dto.response.SupplyDetailResponse;
import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.service.SupplyService;

@RestController
@Slf4j
public class SupplyController {
    
    @Autowired
    SupplyService supplyService;

    @PostMapping(
        path = "/api/supplies",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<SupplyResponse>> createSupply(@Valid SupplyCreateRequest request){
        SupplyResponse resultFromService = supplyService.createSupply(request);
 
        WebResponse<SupplyResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);   
    }

    @GetMapping(
        path = "/api/supplies/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<SupplyDetailResponse>> getSupply(@PathVariable(name = "id") UUID id){
        SupplyDetailResponse resultFromService = supplyService.getSupply(id);
        
        WebResponse<SupplyDetailResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/supplies/search",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<SupplyResponse>>> getSupplys(@ModelAttribute @Valid SupplyGetByFilter request){
        Slice<SupplyResponse> resultFromService = supplyService.searchSuppliesByRequests(request);
        
        WebResponse<Slice<SupplyResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    } 

    @PostMapping(
        path = "/api/supplies/{id}/cancel",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<SupplyResponse>> cancelSupply(@PathVariable(name = "id") UUID id){
        SupplyResponse resultFromService = supplyService.cancelSupply(id);
 
        WebResponse<SupplyResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);   
    } 

    @PatchMapping(
        path = "/api/supplies/{id}",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<SupplyResponse>> updateSupplierAPI(@PathVariable(name = "id") UUID id, @Valid SupplyUpdateRequest request){ 
        SupplyResponse resultFromService = supplyService.updateSupply(id, request);

        WebResponse<SupplyResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

    @PostMapping(
        path = "/api/supplies/{id}/refund",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<SupplyResponse>> refundSupplyItem(@PathVariable(name = "id") UUID id, @Valid ItemRefundRequest request){
        SupplyResponse resultFromService = supplyService.refundSupplyItem(id, request);
 
        WebResponse<SupplyResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);   
    } 
}
