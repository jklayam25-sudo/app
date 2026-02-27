package lumi.insert.app.service;

import java.util.UUID;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.StockCardCreateRequest;
import lumi.insert.app.dto.request.StockCardGetByFilter;
import lumi.insert.app.dto.response.StockCardResponse;

public interface StockCardService {

    StockCardResponse createStockCard(StockCardCreateRequest request);

    StockCardResponse getStockCard(UUID id);
  
    Slice<StockCardResponse> getStockCards(UUID lastId, PaginationRequest request);

    // Slice<StockCardResponse> getStockCardByProductId(Long id, UUID lastId, PaginationRequest request);

    Slice<StockCardResponse> searchStockCards(StockCardGetByFilter request);
}
