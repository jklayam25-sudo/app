package lumi.insert.app.service;

import lumi.insert.app.dto.request.ProductCreateRequest;
import lumi.insert.app.dto.response.ProductCreateResponse;
import lumi.insert.app.dto.response.ProductStockResponse;

public interface ProductService {

    ProductCreateResponse createProduct(ProductCreateRequest request);

    ProductStockResponse getProductStock(Long productId);

}
