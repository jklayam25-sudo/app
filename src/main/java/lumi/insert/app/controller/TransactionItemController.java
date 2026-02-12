package lumi.insert.app.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lumi.insert.app.controller.wrapper.WebResponse; 
import lumi.insert.app.dto.request.TransactionItemCreateRequest;
import lumi.insert.app.dto.response.TransactionItemResponse; 
import lumi.insert.app.service.TransactionItemService;

@RestController
public class TransactionItemController {
    
    @Autowired
    TransactionItemService transactionItemService;

    @PostMapping(
        path = "/api/transactions/{id}/items",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<TransactionItemResponse>> createTransactionItem(@PathVariable(name = "id") UUID id, @Valid TransactionItemCreateRequest request){
        TransactionItemResponse resultFromService = transactionItemService.createTransactionItem(id, request);
 
        WebResponse<TransactionItemResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);   
    }

    @GetMapping(
        path = "/api/transactions/{id}/items",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<TransactionItemResponse>> getTransactionItem(@PathVariable(name = "id") UUID id){
        TransactionItemResponse resultFromService = transactionItemService.getTransactionItem(id);
 
        WebResponse<TransactionItemResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }
}
