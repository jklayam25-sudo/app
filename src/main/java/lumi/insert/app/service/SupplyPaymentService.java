package lumi.insert.app.service;

import java.util.UUID;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.SupplyPaymentCreateRequest;
import lumi.insert.app.dto.request.SupplyPaymentGetByFilter;
import lumi.insert.app.dto.response.SupplyPaymentResponse;

public interface SupplyPaymentService {
    
    SupplyPaymentResponse createSupplyPayment(UUID supplyId, SupplyPaymentCreateRequest request);

    Slice<SupplyPaymentResponse> getSupplyPaymentsBySupplyId(UUID supplyId, PaginationRequest request);

    SupplyPaymentResponse getSupplyPayment(UUID id);

    Slice<SupplyPaymentResponse> getSupplyPaymentsByRequests(SupplyPaymentGetByFilter request);

    SupplyPaymentResponse refundSupplyPayment(UUID supplyId, SupplyPaymentCreateRequest request);

}
