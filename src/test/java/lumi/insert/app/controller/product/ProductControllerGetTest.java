package lumi.insert.app.controller.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.ProductGetByFilter;
import lumi.insert.app.dto.request.ProductGetNameRequest;
import lumi.insert.app.dto.response.ProductName;
import lumi.insert.app.dto.response.ProductResponse;
import lumi.insert.app.entity.Product;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.utils.forTesting.ProductUtils;

public class ProductControllerGetTest extends BaseProductControllerTest{
    @Test
    public void getProductAPI_shouldSuccessAndReturningJson() throws Exception{
        ProductResponse productResponse = ProductResponse.builder()
        .id(1L)
        .name("ProductMock")
        .basePrice(1000L)
        .build();

        when(productService.getProductById(1L)).thenReturn(productResponse);

        mockMvc.perform(
            get("/api/products/1")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.name").value(productResponse.name()))
        .andExpect(jsonPath("$.data.basePrice").value(1000L));
    }

    @Test
    public void getProductAPI_shouldThrownNotFound() throws Exception{
        when(productService.getProductById(1L)).thenThrow(new NotFoundEntityException("Product with ID " + 1L + " was not found"));

        mockMvc.perform(
            get("/api/products/1")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors").value("Product with ID 1 was not found"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void getProductAPI_shouldThrownTypeMissMatch() throws Exception{
        mockMvc.perform(
            get("/api/products/were")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").value("id must be Long"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void getProductsNameAPI_shouldReturnListOf() throws Exception{
        Slice<Product> mockSliceProduct = ProductUtils.getMockSliceProduct();

        Slice<ProductName> map = mockSliceProduct.map(product -> {
            ProductName productNameResponse = ProductName.builder()
                .id(product.getId())
                .name(product.getName())
                .build();
        
            return productNameResponse;
        });

        when(productService.getAllProductNames(any(ProductGetNameRequest.class))).thenReturn(map);

        mockMvc.perform(
            get("/api/products/searchName?name=pro")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content.length()").value(12))
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    public void getProductsNameAPI_shouldThrownMethodArgsException() throws Exception{       
        mockMvc.perform(
            get("/api/products/searchName?name=")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isBadRequest()) 
        .andExpect(jsonPath("$.errors").value("Name cannot be empty"));
    }

    @Test
    public void getProductByFilterAPI_shouldReturnListOf() throws Exception{
        ArgumentCaptor<ProductGetByFilter> captor = ArgumentCaptor.forClass(ProductGetByFilter.class);

        Slice<Product> mockSliceProduct = ProductUtils.getMockSliceProduct();
        Slice<ProductResponse> map = mockSliceProduct.map(productMapper::createDtoResponseFromProduct);

        when(productService.getProductsByRequests(any())).thenReturn(map);

        mockMvc.perform(
            get("/api/products/filter?name=Pro&maxPrice=100000&sortBy=sellPrice")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isOk()) 
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content.length()").value(12));

        verify(productService).getProductsByRequests(captor.capture());
        ProductGetByFilter value = captor.getValue();

        assertEquals(0, value.getPage());
        assertEquals(10, value.getSize());
        assertEquals("Pro", value.getName());
        assertEquals(null, value.getCategoryId());
        assertEquals("sellPrice", value.getSortBy());
        assertEquals(100000, value.getMaxPrice());
    }

    @Test
    public void getProductByFilterAPI_shouldThrownNotFound() throws Exception{
         when(productService.getProductsByRequests(any())).thenThrow(new NotFoundEntityException("Category with ID " + 1L + " was not found"));

         mockMvc.perform(
            get("/api/products/filter?categoryId=1")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors").value("Category with ID 1 was not found"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void getProductByFilterAPI_shouldThrownTypeMissMatch() throws Exception{
         mockMvc.perform(
            get("/api/products/filter?categoryId=true")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isBadRequest()) 
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void getAllProductsAPI_withParam_shouldReturnListOf() throws Exception{
        ArgumentCaptor<PaginationRequest> captor = ArgumentCaptor.forClass(PaginationRequest.class);

        Slice<Product> mockSliceProduct = ProductUtils.getMockSliceProduct();
        Slice<ProductResponse> map = mockSliceProduct.map(productMapper::createDtoResponseFromProduct);

        when(productService.getAllProducts(any())).thenReturn(map);

        mockMvc.perform(
            get("/api/products?size=12")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isOk()) 
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content.length()").value(12));

        verify(productService).getAllProducts(captor.capture());
        PaginationRequest value = captor.getValue();

        assertEquals(0, value.getPage());
        assertEquals(12, value.getSize());
    }

    @Test
    public void getAllProductsAPI_withoutParam_shouldReturnListOf() throws Exception{
        ArgumentCaptor<PaginationRequest> captor = ArgumentCaptor.forClass(PaginationRequest.class);

        Slice<Product> mockSliceProduct = ProductUtils.getMockSliceProduct();
        Slice<ProductResponse> map = mockSliceProduct.map(productMapper::createDtoResponseFromProduct);

        when(productService.getAllProducts(any())).thenReturn(map);

        mockMvc.perform(
            get("/api/products")
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
        .andDo(print())
        .andExpect(status().isOk()) 
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content.length()").value(12));

        verify(productService).getAllProducts(captor.capture());
        PaginationRequest value = captor.getValue();

        assertEquals(0, value.getPage());
        assertEquals(10, value.getSize());
    }
}
