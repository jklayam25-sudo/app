package lumi.insert.app.dto.response;

import java.util.UUID;

public record TransactionPaymentResponse(UUID id, UUID transactionId, Long totalPayment, String paymentFrom, String paymentTo) {
    
}
