package lumi.insert.app.dto.response;

import java.util.UUID;

public record SupplyPaymentResponse(UUID id, UUID supplyId, Long totalPayment, String paymentFrom, String paymentTo, Boolean isForRefund) {
    
}
