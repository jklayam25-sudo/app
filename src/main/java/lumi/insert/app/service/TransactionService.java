package lumi.insert.app.service;

import java.util.UUID;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.TransactionCreateRequest;
import lumi.insert.app.dto.response.TransactionResponse;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionCreateRequest request);

    Slice<TransactionResponse> searchTransactionsByRequests(TransactionCreateRequest request);

    TransactionResponse setTransactionToProcess(UUID id);

    TransactionResponse setTransactionToComplete(UUID id);

    TransactionResponse cancelTransaction(UUID id);

    TransactionResponse getTransaction(UUID id);

    TransactionResponse refreshTransaction(UUID id);

    byte[] getInvoicePdf(UUID id);

}
