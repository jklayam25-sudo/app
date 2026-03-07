package lumi.insert.app.service.implement;
 

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.dto.request.ItemRefundRequest;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.TransactionItemCreateRequest;
import lumi.insert.app.dto.response.TransactionItemDelete;
import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.entity.Customer;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.StockCard;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.entity.nondatabase.StockMove;
import lumi.insert.app.entity.nondatabase.TransactionStatus;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.StockCardRepository;
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
    StockCardRepository stockCardRepository;

    @Autowired
    AllTransactionMapper allTransactionMapper;

    @Override
    public TransactionItemResponse createTransactionItem(UUID transactionId, TransactionItemCreateRequest request) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + transactionId + " was not found"));
        
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new NotFoundEntityException("Product with ID " + request.getProductId() + " was not found"));

        if(product.getStockQuantity() < request.getQuantity()) throw new TransactionValidationException("Product stocks with ID " + request.getProductId() + " doesn't meet buyer quantity, stock left: " + product.getStockQuantity());
 
        TransactionItem transactionItem = TransactionItem.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .price(product.getSellPrice())
            .quantity(request.getQuantity())
            .description(product.getName())
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
 
        if (transaction.getStatus() != TransactionStatus.PENDING) throw new ForbiddenRequestException("Unable to delete the item because Transaction Status is not PENDING(CART)");

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

        if(product.getStockQuantity() < quantity) throw new TransactionValidationException("Product stocks with ID " + product.getId() + " doesn't meet buyer quantity, stock left: " + product.getStockQuantity());

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
    public Slice<TransactionItemResponse> getTransactionByTransactionIdAndProductId(UUID transactionId, Long ProductId) {
        List<TransactionItem> searchedTransactionItem = transactionItemRepository.findByTransactionIdAndProductId(transactionId, ProductId);
        
        Slice<TransactionItem> slices = new SliceImpl<>(searchedTransactionItem);
        return slices.map(allTransactionMapper::createTransactionItemResponseDto); 
    }

    @Override
    public TransactionItemResponse refundTransactionItem(UUID id, ItemRefundRequest request) { 

        List<TransactionItem> itemsWithMatchProduct = transactionItemRepository.findByTransactionIdAndProductId(id, request.getProductId());

        if(itemsWithMatchProduct.size() == 0) throw new NotFoundEntityException("Unable to find any transaction item with product id " + request.getProductId()); 

        long ttlRefundLeft = itemsWithMatchProduct.stream().mapToLong(item -> item.getQuantity()).sum();

        if(ttlRefundLeft < request.getQuantity()) throw new ForbiddenRequestException("Refund quantity is more than actual bought, use valid quantity");

        TransactionItem baseTransactionItem = itemsWithMatchProduct.getFirst();
        Transaction transaction = baseTransactionItem.getTransaction();
        
        if (transaction.getStatus() != TransactionStatus.PROCESS && transaction.getStatus() != TransactionStatus.COMPLETE ) throw new ForbiddenRequestException("Couldn't refund the item because Transaction Status is not PROCESS OR COMPLETE");

        Product product = baseTransactionItem.getProduct();;
        Customer customer = transaction.getCustomer(); 

        Long oldStock = product.getStockQuantity();

        product.setStockQuantity(product.getStockQuantity() + request.getQuantity());

        Long customerRefund = request.getQuantity() * baseTransactionItem.getPrice();

        if(transaction.getTotalUnpaid() - customerRefund < 0){
            customer.setTotalUnpaid(customer.getTotalUnpaid() - transaction.getTotalUnpaid());
            Long balanceLeft = customerRefund - transaction.getTotalUnpaid();
            customer.setTotalUnrefunded(customer.getTotalUnrefunded() + balanceLeft);
            customer.setTotalPaid(customer.getTotalPaid() - balanceLeft);

            transaction.setTotalUnrefunded(transaction.getTotalUnrefunded() + balanceLeft);
            transaction.setTotalUnpaid(0L);
            transaction.setTotalPaid(transaction.getTotalPaid() - balanceLeft);
            transaction.setStatus(TransactionStatus.PROCESS);

        } else {
            transaction.setTotalUnpaid(transaction.getTotalUnpaid() - customerRefund);
            customer.setTotalUnpaid(customer.getTotalUnpaid() - customerRefund);
        }

        TransactionItem refundTransactionItem = TransactionItem.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .price(baseTransactionItem.getPrice())
            .quantity(-request.getQuantity())
            .description("REFUND: " + product.getName())
            .product(product)
            .transaction(transaction)
            .build();

        StockCard stockCard = StockCard.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .referenceId(refundTransactionItem.getId())
            .product(product)
            .productName(product.getName())
            .quantity(request.getQuantity())
            .oldStock(oldStock)
            .newStock(product.getStockQuantity())
            .type(StockMove.CUSTOMER_IN)
            .oldPrice(product.getBasePrice())
            .newPrice(product.getBasePrice())
            .description("Transaction Cancelled, Product refunded. Status: CUSTOMER_IN(IN)")
            .build();

        stockCardRepository.save(stockCard); 
  
        TransactionItem savedRefundTransactionItem = transactionItemRepository.save(refundTransactionItem);
        TransactionItemResponse transactionItemResponseDto = allTransactionMapper.createTransactionItemResponseDto(savedRefundTransactionItem);
        return transactionItemResponseDto;
    }

    @Override
    public TransactionItemResponse getTransactionItem(UUID id) {
        TransactionItem transactionItem = transactionItemRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction Items with ID " + id + " was not found"));

        TransactionItemResponse transactionItemResponseDto = allTransactionMapper.createTransactionItemResponseDto(transactionItem);
        return transactionItemResponseDto;
    }
    
}
