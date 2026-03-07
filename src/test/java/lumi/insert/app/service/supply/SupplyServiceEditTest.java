package lumi.insert.app.service.supply;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.f4b6a3.uuid.UuidCreator;

import lumi.insert.app.dto.request.ItemRefundRequest;
import lumi.insert.app.dto.request.SupplyUpdateRequest;
import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.entity.Supply;
import lumi.insert.app.entity.SupplyItem;
import lumi.insert.app.entity.nondatabase.SupplyStatus;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;

public class SupplyServiceEditTest extends BaseSupplyServiceTest{
    
    @Test
    @DisplayName("Should throw ForbiddenRequestException when trx status isn't pending > setToProcess only from pending")
    public void cancelSupply_statusNonPending_throwNotFound(){
        setupSupply.setStatus(SupplyStatus.CANCELLED);
        when(supplyRepositoryMock.findByIdDetail(any(UUID.class))).thenReturn(Optional.of(setupSupply));

        assertThrows(ForbiddenRequestException.class, ()-> supplyServiceMock.cancelSupply(setupSupply.getId()));
    }

    @Test
    @DisplayName("Should throw NotFoundEntityException when id is invalid for Complete")
    public void cancelSupply_invalidId_throwNotFound() {
        when(supplyRepositoryMock.findByIdDetail(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> supplyServiceMock.cancelSupply(setupSupply.getId()));
    }

    @Test
    @DisplayName("Should cancel and add product stock when cancelTrx complete")
    public void cancelSupply_validRequest_returnDtoAndReverseProduct() {
        setupSupplier.setTotalPaid(230L);
        setupSupplier.setTotalUnpaid(3000L);
        setupSupplier.setTotalUnrefunded(9L);

        setupProduct.setStockQuantity(7L);
        setupProduct.setSellPrice(1000L);
        setupProduct.setBasePrice(800L);

        setupSupplyItem.setProduct(setupProduct);
        setupSupplyItem.setQuantity(4L);
        setupSupplyItem.setPrice(820L);

        setupSupply.setTotalUnpaid(3000L);
        setupSupply.setTotalPaid(200L);
        setupSupply.getSupplyItems().add(setupSupplyItem);
        setupSupply.setSupplier(setupSupplier);
        setupSupply.setStatus(SupplyStatus.UNPAID);
 
        when(supplyRepositoryMock.findByIdDetail(any())).thenReturn(Optional.of(setupSupply));

        SupplyResponse cancelSupply = supplyServiceMock.cancelSupply(UUID.randomUUID());
        assertEquals(SupplyStatus.CANCELLED, cancelSupply.status());
        assertEquals(0L, cancelSupply.totalPaid());
        assertEquals(0L, cancelSupply.totalUnpaid());
        assertEquals(200L, cancelSupply.totalUnrefunded());

        assertEquals(30L, setupSupplier.getTotalPaid());
        assertEquals(0L, setupSupplier.getTotalUnpaid());
        assertEquals(209L, setupSupplier.getTotalUnrefunded());

        assertEquals(773L, setupProduct.getBasePrice());
        assertEquals(3L, setupProduct.getStockQuantity()); 
    }

    @Test
    @DisplayName("Should cancel and add product stock when cancelTrx complete")
    public void cancelSupply_validRequest2_returnDtoAndReverseProduct() {
        setupSupplier.setTotalPaid(230L);
        setupSupplier.setTotalUnpaid(1400L);
        setupSupplier.setTotalUnrefunded(9L);

        setupProduct.setStockQuantity(2L);
        setupProduct.setSellPrice(1000L);
        setupProduct.setBasePrice(800L);

        setupSupplyItem.setProduct(setupProduct);
        setupSupplyItem.setQuantity(4L);
        setupSupplyItem.setPrice(820L);

        SupplyItem partialRefund = SupplyItem.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .product(setupProduct)
        .quantity(-2L)
        .price(820L)
        .build();

        setupSupply.setTotalUnpaid(1400L);
        setupSupply.setTotalUnrefunded(0L);
        setupSupply.setTotalPaid(200L);
        setupSupply.getSupplyItems().add(setupSupplyItem);
        setupSupply.getSupplyItems().add(partialRefund);
        setupSupply.setSupplier(setupSupplier);
        setupSupply.setStatus(SupplyStatus.UNPAID);
 
        when(supplyRepositoryMock.findByIdDetail(any())).thenReturn(Optional.of(setupSupply));

        SupplyResponse cancelSupply = supplyServiceMock.cancelSupply(UUID.randomUUID());
        assertEquals(SupplyStatus.CANCELLED, cancelSupply.status());
        assertEquals(0L, cancelSupply.totalPaid());
        assertEquals(0L, cancelSupply.totalUnpaid());
        assertEquals(200L, cancelSupply.totalUnrefunded());

        assertEquals(30L, setupSupplier.getTotalPaid());
        assertEquals(0L, setupSupplier.getTotalUnpaid());
        assertEquals(209L, setupSupplier.getTotalUnrefunded());

        assertEquals(800L, setupProduct.getBasePrice());
        assertEquals(0L, setupProduct.getStockQuantity()); 
    }

    @Test
    @DisplayName("Should cancel and add product stock when cancelTrx complete")
    public void cancelSupply_validRequest3_returnDtoAndReverseProduct() {
        setupSupplier.setTotalPaid(30L);
        setupSupplier.setTotalUnpaid(0L);
        setupSupplier.setTotalUnrefunded(209L);

        setupProduct.setStockQuantity(0L);
        setupProduct.setSellPrice(1000L);
        setupProduct.setBasePrice(800L);

        setupSupplyItem.setProduct(setupProduct);
        setupSupplyItem.setQuantity(4L);
        setupSupplyItem.setPrice(820L);

        SupplyItem fullRefund = SupplyItem.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .product(setupProduct)
        .quantity(-4L)
        .price(820L)
        .build();

        setupSupply.setTotalUnpaid(0L);
        setupSupply.setTotalUnrefunded(200L);
        setupSupply.setTotalPaid(0L);
        setupSupply.getSupplyItems().add(setupSupplyItem);
        setupSupply.getSupplyItems().add(fullRefund);
        setupSupply.setSupplier(setupSupplier);
        setupSupply.setStatus(SupplyStatus.UNPAID);
 
        when(supplyRepositoryMock.findByIdDetail(any())).thenReturn(Optional.of(setupSupply));

        SupplyResponse cancelSupply = supplyServiceMock.cancelSupply(UUID.randomUUID());
        assertEquals(SupplyStatus.CANCELLED, cancelSupply.status());
        assertEquals(0L, cancelSupply.totalPaid());
        assertEquals(0L, cancelSupply.totalUnpaid());
        assertEquals(200L, cancelSupply.totalUnrefunded());

        assertEquals(30L, setupSupplier.getTotalPaid());
        assertEquals(0L, setupSupplier.getTotalUnpaid());
        assertEquals(209L, setupSupplier.getTotalUnrefunded());

        assertEquals(800L, setupProduct.getBasePrice());
        assertEquals(0L, setupProduct.getStockQuantity()); 
    }

    @Test
    @DisplayName("Update Supply: Discount exceeds unpaid amount")
    public void updateSupply_extremeDiscount() {
        UUID id = UUID.randomUUID();
        
        setupSupplier.setTotalUnpaid(1000L);
        setupSupplier.setTotalPaid(500L);
        setupSupplier.setTotalUnrefunded(0L);
 
        Supply supply = Supply.builder()
                .subTotal(200L)
                .grandTotal(200L)
                .totalUnpaid(40L)
                .totalPaid(160L)
                .totalUnrefunded(0L)
                .supplier(setupSupplier)
                .build();

        SupplyUpdateRequest request = SupplyUpdateRequest.builder()
                .totalDiscount(50L)
                .totalFee(0L)
                .build();

        when(supplyRepositoryMock.findByIdDetail(id)).thenReturn(Optional.of(supply));

        SupplyResponse updateSupply = supplyServiceMock.updateSupply(id, request);;

        assertEquals(150L, updateSupply.grandTotal());
        assertEquals(0L, updateSupply.totalUnpaid());
        assertEquals(150L, updateSupply.totalPaid());
        assertEquals(10L, updateSupply.totalUnrefunded());

        assertEquals(150L, supply.getGrandTotal());
        assertEquals(0L, supply.getTotalUnpaid());
        assertEquals(150L, supply.getTotalPaid());

        assertEquals(950L, setupSupplier.getTotalUnpaid());
        assertEquals(490L, setupSupplier.getTotalPaid());
        assertEquals(10L, setupSupplier.getTotalUnrefunded());
    }

    @Test
    @DisplayName("Update Supply: Fee increases total cost")
    public void updateSupply_feeIncrease() {
        UUID id = UUID.randomUUID();
        
        setupSupplier.setTotalUnpaid(1000L);
        setupSupplier.setTotalPaid(500L);
        setupSupplier.setTotalUnrefunded(0L);

        Supply supply = Supply.builder()
                .subTotal(200L)
                .grandTotal(200L)
                .totalUnpaid(40L)
                .totalPaid(160L)
                .totalUnrefunded(0L)
                .supplier(setupSupplier)
                .build();

        SupplyUpdateRequest request = SupplyUpdateRequest.builder()
                .totalDiscount(0L)
                .totalFee(50L)
                .build();

        when(supplyRepositoryMock.findByIdDetail(id)).thenReturn(Optional.of(supply));

        SupplyResponse updateSupply = supplyServiceMock.updateSupply(id, request);;

        assertEquals(250L, updateSupply.grandTotal());
        assertEquals(90L, updateSupply.totalUnpaid());
        assertEquals(160L, updateSupply.totalPaid()); 

        assertEquals(250L, supply.getGrandTotal());
        assertEquals(90L, supply.getTotalUnpaid());
        assertEquals(160L, supply.getTotalPaid());

        assertEquals(1050L, setupSupplier.getTotalUnpaid());
        assertEquals(500L, setupSupplier.getTotalPaid());
    }

    @Test
    @DisplayName("Update Supply: Fee increases total cost")
    public void refundSupplyItem_validRequest_returnAndCalculate() { 
        
        setupSupplier.setTotalUnpaid(1000L);
        setupSupplier.setTotalPaid(500L);
        setupSupplier.setTotalUnrefunded(0L);

        Supply supply = Supply.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .subTotal(1500L)
            .grandTotal(1500L)
            .totalUnpaid(1000L)
            .totalPaid(500L)
            .totalUnrefunded(0L)
            .supplier(setupSupplier)
            .build();

        setupProduct.setStockQuantity(3L);
        setupProduct.setBasePrice(510L);

        setupSupplyItem.setPrice(500L);
        setupSupplyItem.setQuantity(3L);
        setupSupplyItem.setProduct(setupProduct);
        setupSupplyItem.setSupply(supply);


        ItemRefundRequest request = ItemRefundRequest.builder()
                .productId(setupProduct.getId())
                .quantity(2L) 
                .build();

        when(supplyItemRepositoryMock.findBySupplyIdAndProductId(supply.getId(), request.getProductId())).thenReturn(List.of(setupSupplyItem));

        SupplyResponse updateSupply = supplyServiceMock.refundSupplyItem(supply.getId(), request);;

        assertEquals(0L, updateSupply.totalUnrefunded());
        assertEquals(0L, updateSupply.totalUnpaid());
        assertEquals(500L, updateSupply.totalPaid()); 

        assertEquals(0L, supply.getTotalUnrefunded());
        assertEquals(0L, supply.getTotalUnpaid());
        assertEquals(500L, supply.getTotalPaid());

        assertEquals(0L, setupSupplier.getTotalUnpaid());
        assertEquals(500L, setupSupplier.getTotalPaid());

        assertEquals(530L, setupProduct.getBasePrice());
        assertEquals(1L, setupProduct.getStockQuantity());
    }

    @Test
    @DisplayName("Update Supply: Fee increases total cost")
    public void refundSupplyItem_validRequest2_returnAndCalculate() { 
        
        setupSupplier.setTotalUnpaid(500L);
        setupSupplier.setTotalPaid(500L);
        setupSupplier.setTotalUnrefunded(0L);

        Supply supply = Supply.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .subTotal(1500L)
            .grandTotal(1500L)
            .totalUnpaid(500L)
            .totalPaid(500L)
            .totalUnrefunded(0L)
            .supplier(setupSupplier)
            .build();

        setupProduct.setStockQuantity(2L);
        setupProduct.setBasePrice(515L);

        setupSupplyItem.setPrice(500L);
        setupSupplyItem.setQuantity(3L);
        setupSupplyItem.setProduct(setupProduct);
        setupSupplyItem.setSupply(supply);

        SupplyItem refundItem = SupplyItem.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .price(500L)
        .quantity(-1L)
        .supply(supply)
        .product(setupProduct)
        .build();

        ItemRefundRequest request = ItemRefundRequest.builder()
                .productId(setupProduct.getId())
                .quantity(1L) 
                .build();

        when(supplyItemRepositoryMock.findBySupplyIdAndProductId(supply.getId(), request.getProductId())).thenReturn(List.of(setupSupplyItem, refundItem));

        SupplyResponse updateSupply = supplyServiceMock.refundSupplyItem(supply.getId(), request);;

        assertEquals(0L, updateSupply.totalUnrefunded());
        assertEquals(0L, updateSupply.totalUnpaid());
        assertEquals(500L, updateSupply.totalPaid()); 

        assertEquals(0L, supply.getTotalUnrefunded());
        assertEquals(0L, supply.getTotalUnpaid());
        assertEquals(500L, supply.getTotalPaid());

        assertEquals(0L, setupSupplier.getTotalUnpaid());
        assertEquals(500L, setupSupplier.getTotalPaid());

        assertEquals(530L, setupProduct.getBasePrice());
        assertEquals(1L, setupProduct.getStockQuantity());
    }

    @Test
    @DisplayName("Update Supply: Fee increases total cost")
    public void refundSupplyItem_notFound_returnAndCalculate() { 
        Supply supply = Supply.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .build();

        ItemRefundRequest request = ItemRefundRequest.builder()
                .productId(setupProduct.getId())
                .quantity(1L) 
                .build();

        when(supplyItemRepositoryMock.findBySupplyIdAndProductId(supply.getId(), request.getProductId())).thenReturn(List.of());

        assertThrows(NotFoundEntityException.class, () -> supplyServiceMock.refundSupplyItem(supply.getId(), request));
    }

    @Test
    @DisplayName("Update Supply: Fee increases total cost")
    public void refundSupplyItem_refundQuantityExceedRealQuantity_returnAndCalculate() { 
        Supply supply = Supply.builder()
            .id(UuidCreator.getTimeOrderedEpochFast()) 
            .build();

        setupProduct.setStockQuantity(2L); 
 
        setupSupplyItem.setQuantity(3L); 

        SupplyItem refundItem = SupplyItem.builder()
        .id(UuidCreator.getTimeOrderedEpochFast()) 
        .quantity(-1L) 
        .build();

        ItemRefundRequest request = ItemRefundRequest.builder()
                .productId(setupProduct.getId())
                .quantity(3L) 
                .build();

        when(supplyItemRepositoryMock.findBySupplyIdAndProductId(supply.getId(), request.getProductId())).thenReturn(List.of(setupSupplyItem, refundItem));

        assertThrows(ForbiddenRequestException.class, () -> supplyServiceMock.refundSupplyItem(supply.getId(), request));
    }

    @Test
    @DisplayName("Update Supply: Fee increases total cost")
    public void refundSupplyItem_supplyAlrCancelled_returnAndCalculate() { 
        Supply supply = Supply.builder()
            .id(UuidCreator.getTimeOrderedEpochFast()) 
            .status(SupplyStatus.CANCELLED)
            .build();

        setupProduct.setStockQuantity(2L); 
 
        setupSupplyItem.setQuantity(3L); 
        setupSupplyItem.setSupply(supply);

        SupplyItem refundItem = SupplyItem.builder()
        .id(UuidCreator.getTimeOrderedEpochFast()) 
        .quantity(-1L)
        .supply(supply)
        .product(setupProduct)
        .build();

        ItemRefundRequest request = ItemRefundRequest.builder()
                .productId(setupProduct.getId())
                .quantity(1L) 
                .build();

        when(supplyItemRepositoryMock.findBySupplyIdAndProductId(supply.getId(), request.getProductId())).thenReturn(List.of(setupSupplyItem, refundItem));

        assertThrows(ForbiddenRequestException.class, () -> supplyServiceMock.refundSupplyItem(supply.getId(), request));
    }

    @Test
    @DisplayName("Update Supply: Fee increases total cost")
    public void refundSupplyItem_outOfStockToRefund_returnAndCalculate() {  

        Supply supply = Supply.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .subTotal(1500L)
            .grandTotal(1500L)
            .totalUnpaid(500L)
            .totalPaid(500L)
            .totalUnrefunded(0L)
            .supplier(setupSupplier)
            .build();

        setupProduct.setStockQuantity(0L);
        setupProduct.setBasePrice(515L);

        setupSupplyItem.setPrice(500L);
        setupSupplyItem.setQuantity(3L);
        setupSupplyItem.setProduct(setupProduct);
        setupSupplyItem.setSupply(supply);

        SupplyItem refundItem = SupplyItem.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .price(500L)
        .quantity(-1L)
        .supply(supply)
        .product(setupProduct)
        .build();

        ItemRefundRequest request = ItemRefundRequest.builder()
                .productId(setupProduct.getId())
                .quantity(1L) 
                .build();

        when(supplyItemRepositoryMock.findBySupplyIdAndProductId(supply.getId(), request.getProductId())).thenReturn(List.of(setupSupplyItem, refundItem));

         assertThrows(TransactionValidationException.class, () -> supplyServiceMock.refundSupplyItem(supply.getId(), request));
 
    }
    
    
}
