package lumi.insert.app.service;

import java.util.UUID;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.TransactionItemCreateRequest;
import lumi.insert.app.dto.response.TransactionItemDelete;
import lumi.insert.app.dto.response.TransactionItemResponse; 

public interface TransactionItemService {
    
    TransactionItemResponse createTransactionItem(UUID transactionId, TransactionItemCreateRequest request);

    TransactionItemDelete deleteTransactionItem(UUID id);

    TransactionItemResponse updateTransactionItemQuantity(UUID id, Long quantity);

    Slice<TransactionItemResponse> getTransactionItemsByTransactionId(UUID transactionId, PaginationRequest paginationRequest);

    TransactionItemResponse getTransactionByTransactionIdAndProductId(UUID transactionId, Long ProductId);

    TransactionItemResponse refundTransactionItem(UUID id, Long quantity);

    TransactionItemResponse getTransactionItem(UUID id);

}
