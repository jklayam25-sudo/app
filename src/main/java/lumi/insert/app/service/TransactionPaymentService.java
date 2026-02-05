package lumi.insert.app.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.TransactionPaymentCreateRequest; 
import lumi.insert.app.dto.response.TransactionPaymentResponse;

public interface TransactionPaymentService {

    TransactionPaymentResponse createTransactionPayment(UUID transactionId, TransactionPaymentCreateRequest request);

    Slice<TransactionPaymentResponse> getTransactionsByTransactionId(UUID transactionId);

    TransactionPaymentResponse getTransaction(UUID id);

    Slice<TransactionPaymentResponse> getTransactionsByRequests(TransactionPaymentCreateRequest filter, Pageable pageable);
}
