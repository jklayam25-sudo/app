package lumi.insert.app.service.supply;

import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import lumi.insert.app.dto.request.SupplyCreateRequest;
import lumi.insert.app.dto.request.SupplyItemCreate;
import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.entity.StockCard;
import lumi.insert.app.entity.Supplier;
import lumi.insert.app.entity.Supply;
import lumi.insert.app.entity.SupplyItem;
import lumi.insert.app.entity.nondatabase.SupplyStatus;
import lumi.insert.app.exception.NotFoundEntityException;  

public class SupplyServiceCreateTest extends BaseSupplyServiceTest{
    
    @Test
    @DisplayName("Should return SupplyResponse DTO when creating supply is successful")
    public void createSupply_validRequest_returnSupplyResponse(){
        setupProduct.setBasePrice(350L);
        setupProduct.setStockQuantity(0L);

        Supplier supplier = Supplier.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .totalUnpaid(10L)
        .build();

        when(supplyRepositoryMock.saveAndFlush(any(Supply.class))).thenAnswer(i -> i.getArgument(0));
        when(supplierRepositoryMock.findById(supplier.getId())).thenReturn(Optional.of(supplier));

        when(productRepositoryMock.findAllById(List.of(setupProduct.getId()))).thenReturn(List.of(setupProduct));

        SupplyCreateRequest request = SupplyCreateRequest.builder()
        .supplierId(supplier.getId())
        .invoiceId("INV-XXX-XXX")
        .totalFee(0L)
        .totalDiscount(0L)
        .supplyItems(List.of(SupplyItemCreate.builder().productId(setupProduct.getId()).price(400L).quantity(27L).build()))
        .build();

        SupplyResponse supply = supplyServiceMock.createSupply(request);
 
        assertNotNull(supply.invoiceId());
        assertEquals(400L * 27L, supply.grandTotal());
        assertEquals(1L, supply.totalItems());
        assertEquals(400L * 27L, supply.totalUnpaid());
        assertEquals(SupplyStatus.UNPAID, supply.status());

        assertEquals(400L, setupProduct.getBasePrice());
        assertEquals(27L, setupProduct.getStockQuantity());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Iterable<SupplyItem>> supplyItemCaptor = ArgumentCaptor.forClass(Iterable.class);

        verify(supplyItemRepositoryMock, times(1)).saveAll(supplyItemCaptor.capture());
        SupplyItem createdSupplyItem = supplyItemCaptor.getValue().iterator().next();
        assertEquals(400L, createdSupplyItem.getPrice());
        assertEquals(27L, createdSupplyItem.getQuantity());
        assertEquals(setupProduct.getId(), createdSupplyItem.getProduct().getId()); 

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Iterable<StockCard>> stockCardCaptor = ArgumentCaptor.forClass(Iterable.class);
        verify(stockCardRepositoryMock, times(1)).saveAll(stockCardCaptor.capture());
        StockCard createdStockCard = stockCardCaptor.getValue().iterator().next();

        assertEquals(27L, createdStockCard.getNewStock());
        assertEquals(0L, createdStockCard.getOldStock()); 

        assertEquals(400L * 27L + 10L, supplier.getTotalUnpaid());
    }

    @Test
    @DisplayName("Should return SupplyResponse DTO when creating supply is successful, CASE 2: Stock still available, Test of AVG PRICE Sync")
    public void createSupply_validRequest2_returnSupplyResponse(){
        setupProduct.setBasePrice(385L);
        setupProduct.setStockQuantity(13L);

        Supplier supplier = Supplier.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .totalUnpaid(10L)
        .build();

        when(supplyRepositoryMock.saveAndFlush(any(Supply.class))).thenAnswer(i -> i.getArgument(0));
        when(supplierRepositoryMock.findById(supplier.getId())).thenReturn(Optional.of(supplier));

        when(productRepositoryMock.findAllById(List.of(setupProduct.getId()))).thenReturn(List.of(setupProduct));

        SupplyCreateRequest request = SupplyCreateRequest.builder()
        .supplierId(supplier.getId())
        .invoiceId("INV-XXX-XXX")
        .totalFee(0L)
        .totalDiscount(0L)
        .supplyItems(List.of(SupplyItemCreate.builder().productId(setupProduct.getId()).price(400L).quantity(27L).build()))
        .build();

        SupplyResponse supply = supplyServiceMock.createSupply(request);
 
        assertNotNull(supply.invoiceId());
        assertEquals(400L * 27L, supply.grandTotal());
        assertEquals(1L, supply.totalItems());
        assertEquals(400L * 27L, supply.totalUnpaid());
        assertEquals(SupplyStatus.UNPAID, supply.status());

        assertEquals(395L, setupProduct.getBasePrice());
        assertEquals(40L, setupProduct.getStockQuantity());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Iterable<SupplyItem>> supplyItemCaptor = ArgumentCaptor.forClass(Iterable.class);

        verify(supplyItemRepositoryMock, times(1)).saveAll(supplyItemCaptor.capture());
        SupplyItem createdSupplyItem = supplyItemCaptor.getValue().iterator().next();
        assertEquals(400L, createdSupplyItem.getPrice());
        assertEquals(27L, createdSupplyItem.getQuantity());
        assertEquals(setupProduct.getId(), createdSupplyItem.getProduct().getId()); 

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Iterable<StockCard>> stockCardCaptor = ArgumentCaptor.forClass(Iterable.class);
        verify(stockCardRepositoryMock, times(1)).saveAll(stockCardCaptor.capture());
        StockCard createdStockCard = stockCardCaptor.getValue().iterator().next();

        assertEquals(40L, createdStockCard.getNewStock());
        assertEquals(13L, createdStockCard.getOldStock()); 

        assertEquals(400L * 27L + 10L, supplier.getTotalUnpaid());
    }

    @Test
    @DisplayName("Should throw notFound when supplier not found")
    public void createSupply_invalidSupplier_throwNotFound(){ 
        when(supplierRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

       assertThrows(NotFoundEntityException.class, () -> supplyServiceMock.createSupply(SupplyCreateRequest.builder().supplierId(setupSupply.getId()).build()));
 
    }

    @Test
    @DisplayName("Should throw notFound when one or more of product request to add not found")
    public void createSupply_requestProductToAddNotFound_throwNotFound(){ 
        Supplier supplier = Supplier.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .totalUnpaid(10L)
        .build();
 
        when(supplierRepositoryMock.findById(supplier.getId())).thenReturn(Optional.of(supplier));

        when(productRepositoryMock.findAllById(anyIterable())).thenReturn(List.of(setupProduct));

        SupplyCreateRequest request = SupplyCreateRequest.builder()
        .supplierId(supplier.getId())
        .invoiceId("INV-XXX-XXX")
        .totalFee(0L)
        .totalDiscount(0L)
        .supplyItems(List.of(
            SupplyItemCreate.builder().productId(setupProduct.getId()).price(400L).quantity(27L).build(),
            SupplyItemCreate.builder().productId(777L).price(400L).quantity(27L).build()
        ))
        .build();

        assertThrows(NotFoundEntityException.class, () -> supplyServiceMock.createSupply(request));
    }

    // @Test
    // @DisplayName("Should throw Tranaction validation exc when supplier is inactive")
    // public void createSupply_inactiveSupplier_throwSupplyValidationj(){ 
    //     Supplier supplier = Supplier.builder()
    //     .id(UuidCreator.getTimeOrderedEpochFast())
    //     .isActive(false)
    //     .build();

    //     when(supplierRepositoryMock.findById(supplier.getId())).thenReturn(Optional.of(supplier));

    //    assertThrows(SupplyValidationException.class, () -> supplyServiceMock.createSupply(SupplyCreateRequest.builder().supplierId(supplier.getId()).build()));
 
    // }
}
