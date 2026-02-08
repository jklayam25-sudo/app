package lumi.insert.app.service.implement;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.TransactionItemCreateRequest;
import lumi.insert.app.dto.response.TransactionItemDelete;
import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.entity.TransactionStatus;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.TransactionItemRepository;
import lumi.insert.app.repository.TransactionRepository;
import lumi.insert.app.service.TransactionItemService;
import lumi.insert.app.utils.mapper.AllTransactionMapper;

@Service
@Transactional
@Slf4j
public class TransactionItemServiceImpl implements TransactionItemService{

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TransactionItemRepository transactionItemRepository;

    @Autowired
    AllTransactionMapper allTransactionMapper;

    @Override
    public TransactionItemResponse createTransactionItem(UUID transactionId, TransactionItemCreateRequest request) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + transactionId + " was not found"));
        
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new NotFoundEntityException("Product with ID " + request.getProductId() + " was not found"));

        if(product.getStockQuantity() < request.getQuantity()) throw new TransactionValidationException("Product stocks with ID " + request.getProductId() + " doesn't meet buyer quantity");
 
        TransactionItem transactionItem = TransactionItem.builder()
        .price(product.getSellPrice())
        .quantity(request.getQuantity())
        .product(product)
        .transaction(transaction)
        .build();

        transactionItemRepository.save(transactionItem);
        
        transaction.setTotalItems(transaction.getTotalItems() + 1);
        transaction.setSubTotal(transaction.getSubTotal() + (transactionItem.getQuantity() * transactionItem.getPrice()));
        transaction.setGrandTotal(transaction.getSubTotal() - transaction.getTotalDiscount() - transaction.getTotalFee());
        
        TransactionItemResponse transactionItemResponseDto = allTransactionMapper.createTransactionItemResponseDto(transactionItem);
        return transactionItemResponseDto;
    }

    @Override
    public TransactionItemDelete deleteTransactionItem(UUID id) {
        TransactionItem transactionItem = transactionItemRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction Items with ID " + id + " was not found"));
        
        Transaction transaction = transactionItem.getTransaction();
 
        if (transaction.getStatus() != TransactionStatus.PENDING) throw new ForbiddenRequestException("Couldn't delete the item because Transaction Status is not PENDING(CART)");

        transaction.setTotalItems(transaction.getTotalItems() - 1);
        transaction.setSubTotal(transaction.getSubTotal() - (transactionItem.getQuantity() * transactionItem.getPrice()));
        transaction.setGrandTotal(transaction.getSubTotal() - transaction.getTotalDiscount() - transaction.getTotalFee());

        transactionItemRepository.delete(transactionItem);
        TransactionItemDelete transactionItemDeleteResponseDto = allTransactionMapper.createTransactionItemDeleteResponseDto(transactionItem);
        return transactionItemDeleteResponseDto;
    }

    @Override
    public TransactionItemResponse updateTransactionItemQuantity(UUID id, Long quantity) {
         TransactionItem transactionItem = transactionItemRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction Items with ID " + id + " was not found"));
        
        Transaction transaction = transactionItem.getTransaction();

        if(transaction.getStatus() != TransactionStatus.PENDING) throw new ForbiddenRequestException("Couldn't update the item because Transaction status is not PENDING(CART)");

        Product product = transactionItem.getProduct();

        if(product.getStockQuantity() < quantity) throw new TransactionValidationException("Product stocks with ID " + product.getId() + " doesn't meet buyer quantity");

        Long transactionItemOldSubTotal = transactionItem.getQuantity() * transactionItem.getPrice();

        transactionItem.setPrice(product.getSellPrice());
        transactionItem.setQuantity(quantity);

        transaction.setSubTotal((transaction.getSubTotal() - transactionItemOldSubTotal) + (transactionItem.getQuantity() * transactionItem.getPrice()));
        transaction.setGrandTotal(transaction.getSubTotal() - transaction.getTotalDiscount() + transaction.getTotalFee());

        TransactionItemResponse transactionItemResponseDto = allTransactionMapper.createTransactionItemResponseDto(transactionItem);
        return transactionItemResponseDto;
    }

    @Override
    public Slice<TransactionItemResponse> getTransactionItemsByTransactionId(UUID transactionId, PaginationRequest request) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()).withSort(sort);

        Slice<TransactionItem> transactionItems = transactionItemRepository.findAllByTransactionId(transactionId, pageable);
        Slice<TransactionItemResponse> result = transactionItems.map(allTransactionMapper::createTransactionItemResponseDto);
        return result;
    }

    @Override
    public TransactionItemResponse getTransactionByTransactionIdAndProductId(UUID transactionId, Long ProductId) {
        TransactionItem searchedTransactionItem = transactionItemRepository.findByTransactionIdAndProductId(transactionId, ProductId)
            .orElseThrow(() -> new NotFoundEntityException("Transaction Items was not found"));
        
       TransactionItemResponse result = allTransactionMapper.createTransactionItemResponseDto(searchedTransactionItem);
        return result;
    }
    
}
