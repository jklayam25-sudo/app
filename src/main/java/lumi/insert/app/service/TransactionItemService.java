package lumi.insert.app.service;

import java.util.UUID;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.TransactionItemCreateRequest;
import lumi.insert.app.dto.response.TransactionItemDelete;
import lumi.insert.app.entity.TransactionItem;

public interface TransactionItemService {
    
    TransactionItem createTransactionItem(UUID transactionId, TransactionItemCreateRequest request);

    TransactionItemDelete deleteTransactionItem(UUID id);

    TransactionItem updateTransactionItemQuantity(UUID id, Long quantity);

    Slice<TransactionItem> getTransactionItemsByTransactionId(UUID transactionId, PaginationRequest paginationRequest);

    TransactionItem getTransactionByTransactionIdAndProductId(UUID transactionId, Long ProductId);
}
