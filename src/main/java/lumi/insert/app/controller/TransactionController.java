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

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.controller.wrapper.WebResponse; 
import lumi.insert.app.dto.request.TransactionCreateRequest;
import lumi.insert.app.dto.request.TransactionGetByFilter;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.service.TransactionService;

@RestController
@Slf4j
public class TransactionController {
    
    @Autowired
    TransactionService transactionService;

    @PostMapping(
        path = "/api/transactions",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<TransactionResponse>> createTransaction(@Valid TransactionCreateRequest request){
        TransactionResponse resultFromService = transactionService.createTransaction(request);
 
        WebResponse<TransactionResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);   
    }

    @GetMapping(
        path = "/api/transactions/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<TransactionResponse>> getTransaction(@PathVariable(name = "id") UUID id){
        TransactionResponse resultFromService = transactionService.getTransaction(id);
        
        WebResponse<TransactionResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/transactions/filter",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<TransactionResponse>>> getTransactions(@ModelAttribute @Valid TransactionGetByFilter request){
        Slice<TransactionResponse> resultFromService = transactionService.searchTransactionsByRequests(request);
        
        WebResponse<Slice<TransactionResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @PostMapping(
        path = "/api/transactions/{id}/process",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<TransactionResponse>> processTransaction(@PathVariable(name = "id") UUID id){
        TransactionResponse resultFromService = transactionService.setTransactionToProcess(id);
 
        WebResponse<TransactionResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);   
    }

    @PostMapping(
        path = "/api/transactions/{id}/cancel",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<TransactionResponse>> cancelTransaction(@PathVariable(name = "id") UUID id){
        TransactionResponse resultFromService = transactionService.cancelTransaction(id);
 
        WebResponse<TransactionResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);   
    }

    @PostMapping(
        path = "/api/transactions/{id}/refresh",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<TransactionResponse>> refreshTransaction(@PathVariable(name = "id") UUID id){
        TransactionResponse resultFromService = transactionService.refreshTransaction(id);
 
        WebResponse<TransactionResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);   
    }
}
