package lumi.insert.app.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.dto.request.CategoryCreateRequest;
import lumi.insert.app.dto.request.CategoryEditRequest;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.CategoryResponse;
import lumi.insert.app.service.CategoryService;

@RestController
public class CategoryController {
    
    @Autowired
    CategoryService categoryService;

    @PostMapping(
        path = "/api/categories",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<CategoryResponse>> createCategory(@Valid CategoryCreateRequest request){
        CategoryResponse resultFromService = categoryService.createCategory(request);

        WebResponse<CategoryResponse> wrappedResult = WebResponse.<CategoryResponse>builder()
        .data(resultFromService)
        .build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);   
    }

    @GetMapping(
        path = "/api/categories",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<CategoryResponse>>> getCategories(@Valid PaginationRequest request){
        Slice<CategoryResponse> resultFromService = categoryService.getCategories(request);

        WebResponse<Slice<CategoryResponse>> wrappedResult = WebResponse.<Slice<CategoryResponse>>builder()
        .data(resultFromService)
        .build();

        return ResponseEntity.ok(wrappedResult);   
    }

    @GetMapping(
        path = "/api/categories/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<CategoryResponse>> getCategoryById(@PathVariable(value = "id") Long id){
        CategoryResponse resultFromService = categoryService.getCategoryById(id);

        WebResponse<CategoryResponse> wrappedResult = WebResponse.<CategoryResponse>builder()
        .data(resultFromService)
        .build();

        return ResponseEntity.ok(wrappedResult);   
    }

    @PutMapping(
        path = "/api/categories/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<CategoryResponse>> editCategory(@PathVariable(value = "id", required = true) Long id, CategoryEditRequest request){
        request.setId(id);
        CategoryResponse resultFromService = categoryService.editCategoryName(request);

        WebResponse<CategoryResponse> wrappedResult = WebResponse.<CategoryResponse>builder()
        .data(resultFromService)
        .build();

        return ResponseEntity.ok(wrappedResult);   
    }

    @PostMapping(
        path = "/api/categories/{id}/activate",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<CategoryResponse>> activateProduct(@PathVariable(value = "id", required = true) Long id ){ 
        CategoryResponse resultFromService = categoryService.setCategoryActive(id);

        WebResponse<CategoryResponse> wrappedResult = WebResponse.<CategoryResponse>builder()
        .data(resultFromService)
        .build();

        return ResponseEntity.ok(wrappedResult);   
    }

    @PostMapping(
        path = "/api/categories/{id}/deactivate",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<CategoryResponse>> deactivateProduct(@PathVariable(value = "id", required = true) Long id ){ 
        CategoryResponse resultFromService = categoryService.setCategoryInactive(id);

        WebResponse<CategoryResponse> wrappedResult = WebResponse.<CategoryResponse>builder()
        .data(resultFromService)
        .build();

        return ResponseEntity.ok(wrappedResult);   
    }
}
