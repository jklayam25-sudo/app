package lumi.insert.app.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.ProductCreateRequest;
import lumi.insert.app.dto.request.ProductUpdateRequest;
import lumi.insert.app.dto.request.ProductGetByFilter;
import lumi.insert.app.dto.request.ProductGetNameRequest;
import lumi.insert.app.dto.response.ProductDeleteResponse;
import lumi.insert.app.dto.response.ProductName;
import lumi.insert.app.dto.response.ProductResponse;
import lumi.insert.app.dto.response.ProductStockResponse;
import lumi.insert.app.service.ProductService;

@RestController
@Slf4j
public class ProductController {

    @Autowired
    ProductService productService;
    
    @GetMapping(
        path = "/api/products/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<ProductResponse>> getProduct(@PathVariable(value = "id") Long id){
        ProductResponse resultFromService = productService.getProductById(id);

        WebResponse<ProductResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @GetMapping(
        path = "/api/products/searchName",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<ProductName>>> searchProductNames(@Valid @ModelAttribute ProductGetNameRequest request){
        Slice<ProductName> resultFromService = productService.searchProductNames(request);

        WebResponse<Slice<ProductName>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @GetMapping(
        path = "/api/products/filter",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<ProductResponse>>> getProductByFilter(@ModelAttribute ProductGetByFilter request){
        Slice<ProductResponse> resultFromService = productService.getProductsByRequests(request);

        WebResponse<Slice<ProductResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @GetMapping(
        path = "/api/products",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<ProductResponse>>> getProducts(@ModelAttribute PaginationRequest request){
        Slice<ProductResponse> resultFromService = productService.getProducts(request);

        WebResponse<Slice<ProductResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @GetMapping(
        path = "/api/products/{id}/stocks",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<ProductStockResponse>> getProductStock(@PathVariable(value = "id", required = true) Long id ){
        ProductStockResponse resultFromService = productService.getProductStock(id);

        WebResponse<ProductStockResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @PostMapping(
        path = "/api/products",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<ProductResponse>> createProduct(@Valid ProductCreateRequest request){
        ProductResponse resultFromService = productService.createProduct(request);

        WebResponse<ProductResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);   
    }

    @PostMapping(
        path = "/api/products/{id}/activate",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<ProductDeleteResponse>> activateProduct(@PathVariable(value = "id", required = true) Long id ){ 
        ProductDeleteResponse resultFromService = productService.activateProduct(id);

        WebResponse<ProductDeleteResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @PostMapping(
        path = "/api/products/{id}/deactivate",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<ProductDeleteResponse>> deactivateProduct(@PathVariable(value = "id", required = true) Long id ){ 
        ProductDeleteResponse resultFromService = productService.deactivateProduct(id);

        WebResponse<ProductDeleteResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    @PutMapping(
        path = "/api/products/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<ProductResponse>> editProduct(@PathVariable(value = "id", required = true) Long id, @Valid ProductUpdateRequest request){
        request.setId(id);
        ProductResponse resultFromService = productService.updateProduct(request);

        WebResponse<ProductResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);   
    }

    


}
