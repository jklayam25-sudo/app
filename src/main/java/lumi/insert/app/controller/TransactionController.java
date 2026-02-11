package lumi.insert.app.controller;
 
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.controller.wrapper.WebResponse; 
import lumi.insert.app.dto.request.TransactionCreateRequest; 
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

}
