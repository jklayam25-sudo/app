package lumi.insert.app.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid; 
import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.TransactionItemCreateRequest;
import lumi.insert.app.dto.response.TransactionItemDelete;
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
        path = "/api/transactions/{transactionId}/items",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<TransactionItemResponse>>> getTransactionItems(@PathVariable(name = "transactionId") UUID transactionId, @ModelAttribute PaginationRequest request){
        Slice<TransactionItemResponse> resultFromService = transactionItemService.getTransactionItemsByTransactionId(transactionId, request);
 
        WebResponse<Slice<TransactionItemResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @GetMapping(
        path = "/api/transactions/{transactionId}/items/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<TransactionItemResponse>> getTransactionItem(@PathVariable(name = "transactionId") UUID transactionId, @PathVariable(name = "id") UUID id){
        TransactionItemResponse resultFromService = transactionItemService.getTransactionItem(id);
 
        WebResponse<TransactionItemResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @GetMapping(
        path = "/api/transactions/{transactionId}/items/product/{productId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<TransactionItemResponse>> getTransactionItemByProductId(@PathVariable(name = "transactionId") UUID transactionId, @PathVariable(name = "productId") Long productId){
        TransactionItemResponse resultFromService = transactionItemService.getTransactionByTransactionIdAndProductId(transactionId, productId);
 
        WebResponse<TransactionItemResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @DeleteMapping(
        path = "/api/transactions/{transactionId}/items/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<TransactionItemDelete>> DeleteTransactionItem(@PathVariable(name = "transactionId") UUID transactionId, @PathVariable(name = "id") UUID id){
        TransactionItemDelete resultFromService = transactionItemService.deleteTransactionItem(id);
 
        WebResponse<TransactionItemDelete> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.status(HttpStatusCode.valueOf(410)).body(wrappedResult);   
    }

    @PostMapping(
        path = "/api/transactions/{transactionId}/items/{id}/quantity",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<TransactionItemResponse>> UpdateTransactionItemQuantity(@PathVariable(name = "transactionId") UUID transactionId, @PathVariable(name = "id") UUID id, @RequestParam(name = "quantity") Long quantity){
        TransactionItemResponse resultFromService = transactionItemService.updateTransactionItemQuantity(id, quantity);
 
        WebResponse<TransactionItemResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);


        return ResponseEntity.ok(wrappedResult);   
    }

    @PostMapping(
        path = "/api/transactions/{transactionId}/items/{id}/refund",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<TransactionItemResponse>> refundTransactionItem(@PathVariable(name = "transactionId") UUID transactionId, @PathVariable(name = "id") UUID id, @RequestParam(name = "quantity") Long quantity){
        TransactionItemResponse resultFromService = transactionItemService.refundTransactionItem(id, quantity);
 
        WebResponse<TransactionItemResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }
}
