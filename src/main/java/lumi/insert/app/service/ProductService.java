package lumi.insert.app.service;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.ProductCreateRequest;
import lumi.insert.app.dto.request.ProductEditRequest;
import lumi.insert.app.dto.request.ProductGetByFilter;
import lumi.insert.app.dto.request.ProductGetNameRequest;

import lumi.insert.app.dto.response.ProductDeleteResponse;
import lumi.insert.app.dto.response.ProductName;
import lumi.insert.app.dto.response.ProductResponse;
import lumi.insert.app.dto.response.ProductStockResponse;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    ProductStockResponse getProductStock(Long request);

    ProductResponse editProduct(ProductEditRequest request);

    Slice<ProductName> getAllProductNames(ProductGetNameRequest request);

    ProductResponse getProductById(Long id);

    Slice<ProductResponse> getAllProducts(PaginationRequest request);

    Slice<ProductResponse> getProductsByRequests(ProductGetByFilter request);

    ProductDeleteResponse setProductInactive(Long id);

    ProductDeleteResponse setProductActive(Long id);

}
