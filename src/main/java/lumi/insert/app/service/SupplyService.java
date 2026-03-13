package lumi.insert.app.service;

import java.util.UUID;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.ItemRefundRequest;
import lumi.insert.app.dto.request.SupplyCreateRequest;
import lumi.insert.app.dto.request.SupplyGetByFilter; 
import lumi.insert.app.dto.request.SupplyUpdateRequest;
import lumi.insert.app.dto.response.SupplyDetailResponse;
import lumi.insert.app.dto.response.SupplyResponse;

public interface SupplyService {

    SupplyResponse createSupply(SupplyCreateRequest request);

    Slice<SupplyResponse> searchSuppliesByRequests(SupplyGetByFilter request);

    SupplyResponse cancelSupply(UUID id);

    SupplyDetailResponse getSupply(UUID id);

    SupplyResponse updateSupply(UUID id, SupplyUpdateRequest request);

    SupplyResponse refundSupplyItem(UUID id, ItemRefundRequest request);
 
}
