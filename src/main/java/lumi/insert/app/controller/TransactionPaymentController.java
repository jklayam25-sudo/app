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
import lumi.insert.app.dto.request.TransactionPaymentCreateRequest;
import lumi.insert.app.dto.request.TransactionPaymentGetByFilter;
import lumi.insert.app.dto.response.TransactionPaymentResponse; 
import lumi.insert.app.service.TransactionPaymentService;

@RestController
@Transactional
@Slf4j
public class TransactionPaymentController {
    
    @Autowired
    TransactionPaymentService transactionPaymentService;

    @PostMapping(
        path = "/api/transactions/{transactionId}/payments",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<TransactionPaymentResponse>> createTransactionPaymentAPI(@PathVariable(name = "transactionId") UUID transactionId, @ModelAttribute @Valid TransactionPaymentCreateRequest request){
        TransactionPaymentResponse resultFromService = transactionPaymentService.createTransactionPayment(transactionId, request);
        WebResponse<TransactionPaymentResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);
    }

    @PostMapping(
        path = "/api/transactions/{transactionId}/payments/refund",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<TransactionPaymentResponse>> refundTransactionPaymentAPI(@PathVariable(name = "transactionId") UUID transactionId, @ModelAttribute @Valid TransactionPaymentCreateRequest request){
        TransactionPaymentResponse resultFromService = transactionPaymentService.refundTransactionPayment(transactionId, request);
        WebResponse<TransactionPaymentResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);
    }

    @GetMapping(
        path = "/api/transactions/{transactionId}/payments/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<TransactionPaymentResponse>> getTransactionPaymentAPI(@PathVariable(name = "transactionId") UUID transactionId, @PathVariable(name = "id") UUID id){
        TransactionPaymentResponse resultFromService = transactionPaymentService.getTransactionPayment(id);

        WebResponse<TransactionPaymentResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/transactions/{transactionId}/payments",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<TransactionPaymentResponse>>> getTransactionPaymentsAPI(@PathVariable(name = "transactionId") UUID transactionId,@ModelAttribute @Valid PaginationRequest request){
        Slice<TransactionPaymentResponse> resultFromService = transactionPaymentService.getTransactionPaymentsByTransactionId(transactionId, request);

        WebResponse<Slice<TransactionPaymentResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/transactions/{transactionId}/payments/filter",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<TransactionPaymentResponse>>> searchTransactionPaymentsFilter(@PathVariable(name = "transactionId") UUID transactionId, @ModelAttribute @Valid TransactionPaymentGetByFilter request){
        log.info("{}", request);
        Slice<TransactionPaymentResponse> resultFromService = transactionPaymentService.getTransactionPaymentsByRequests(request);

        WebResponse<Slice<TransactionPaymentResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }
}
