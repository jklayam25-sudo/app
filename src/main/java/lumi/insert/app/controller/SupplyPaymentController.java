package lumi.insert.app.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
 
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.SupplyPaymentCreateRequest;
import lumi.insert.app.dto.request.SupplyPaymentGetByFilter;
import lumi.insert.app.dto.response.SupplyPaymentResponse; 
import lumi.insert.app.service.SupplyPaymentService;

@RestController
@Transactional
@Slf4j
public class SupplyPaymentController {
    
    @Autowired
    SupplyPaymentService supplyPaymentService;

    @PostMapping(
        path = "/api/supplies/{supplyId}/payments",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<SupplyPaymentResponse>> createSupplyPaymentAPI(@PathVariable(name = "supplyId") UUID supplyId, @ModelAttribute @Valid SupplyPaymentCreateRequest request){
        SupplyPaymentResponse resultFromService = supplyPaymentService.createSupplyPayment(supplyId, request);
        WebResponse<SupplyPaymentResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);
    }

    @PostMapping(
        path = "/api/supplies/{supplyId}/payments/refund",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<SupplyPaymentResponse>> refundSupplyPaymentAPI(@PathVariable(name = "supplyId") UUID supplyId, @ModelAttribute @Valid SupplyPaymentCreateRequest request){
        SupplyPaymentResponse resultFromService = supplyPaymentService.refundSupplyPayment(supplyId, request);
        WebResponse<SupplyPaymentResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);
    }

    @GetMapping(
        path = "/api/supplies/{supplyId}/payments/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<SupplyPaymentResponse>> getSupplyPaymentAPI(@PathVariable(name = "supplyId") UUID supplyId, @PathVariable(name = "id") UUID id){
        SupplyPaymentResponse resultFromService = supplyPaymentService.getSupplyPayment(id);

        WebResponse<SupplyPaymentResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/supplies/{supplyId}/payments",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<SupplyPaymentResponse>>> getSupplyPaymentsAPI(@PathVariable(name = "supplyId") UUID supplyId,@ModelAttribute @Valid PaginationRequest request){
        Slice<SupplyPaymentResponse> resultFromService = supplyPaymentService.getSupplyPaymentsBySupplyId(supplyId, request);

        WebResponse<Slice<SupplyPaymentResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/supplies/{supplyId}/payments/search",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<SupplyPaymentResponse>>> searchSupplyPaymentsFilter(@PathVariable(name = "supplyId") UUID supplyId, @ModelAttribute @Valid SupplyPaymentGetByFilter request){
        log.info("{}", request);
        Slice<SupplyPaymentResponse> resultFromService = supplyPaymentService.getSupplyPaymentsByRequests(request);

        WebResponse<Slice<SupplyPaymentResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }
}
