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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.StockCardCreateRequest;
import lumi.insert.app.dto.request.StockCardGetByFilter;
import lumi.insert.app.dto.response.StockCardResponse; 
import lumi.insert.app.service.StockCardService;

@RestController
public class StockCardController {

    @Autowired
    StockCardService stockCardService;
    
    @PostMapping(
        path = "/api/stockcards",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<StockCardResponse>> createStockCardAPI(@Valid StockCardCreateRequest request){
        StockCardResponse resultFromService = stockCardService.createStockCard(request);

        WebResponse<StockCardResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);
    }

    @GetMapping(
        path = "/api/stockcards/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<StockCardResponse>> getStockCardAPI(@PathVariable(name = "id") UUID id){
        StockCardResponse resultFromService = stockCardService.getStockCard(id);

        WebResponse<StockCardResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/stockcards",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<StockCardResponse>>> getStockCardsAPI(@RequestParam(name = "lastId", required = false) UUID lastId, @ModelAttribute PaginationRequest request){
        Slice<StockCardResponse> resultFromService = stockCardService.getStockCards(lastId, request);

        WebResponse<Slice<StockCardResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/stockcards/search",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<StockCardResponse>>> searchStockCardsAPI(@ModelAttribute @Valid StockCardGetByFilter request){
        Slice<StockCardResponse> resultFromService = stockCardService.searchStockCards(request);

        WebResponse<Slice<StockCardResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }
}
