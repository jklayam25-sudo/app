package lumi.insert.app.service;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.ProductCreateRequest;
import lumi.insert.app.dto.request.ProductEditRequest;
import lumi.insert.app.dto.request.ProductGetNameRequest;

import lumi.insert.app.dto.response.ProductCreateResponse;
import lumi.insert.app.dto.response.ProductName;
import lumi.insert.app.dto.response.ProductStockResponse;

public interface ProductService {

    ProductCreateResponse createProduct(ProductCreateRequest request);

    ProductStockResponse getProductStock(Long request);

    ProductCreateResponse editProduct(ProductEditRequest request);

    Slice<ProductName> getAllProductNames(ProductGetNameRequest request);

    ProductCreateResponse getProductById(Long id);

    Slice<ProductCreateResponse> getAllProducts(PaginationRequest request);

}
