package lumi.insert.app.service.implement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function; 

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.dto.request.TransactionCreateRequest;
import lumi.insert.app.dto.request.TransactionGetByFilter;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.entity.TransactionStatus;
import lumi.insert.app.exception.BoilerplateRequestException;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.TransactionItemRepository;
import lumi.insert.app.repository.TransactionRepository;
import lumi.insert.app.repository.projection.ProductRefreshProjection;
import lumi.insert.app.service.TransactionService;
import lumi.insert.app.utils.generator.InvoiceGenerator;
import lumi.insert.app.utils.mapper.AllTransactionMapper;

@Service
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ProductRepository productRepository;
    
    @Autowired
    TransactionItemRepository transactionItemRepository;

    @Autowired
    InvoiceGenerator invoiceGenerator;

    @Autowired
    AllTransactionMapper allTransactionMapper;

    @Override
    public TransactionResponse createTransaction(TransactionCreateRequest request) {
        Transaction transaction = Transaction.builder()
        .invoiceId(invoiceGenerator.generate())
        .build();

        log.info("{}", transaction);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return allTransactionMapper.createTransactionResponseDto(savedTransaction);
    }

    @Override
    public Slice<TransactionResponse> searchTransactionsByRequests(TransactionGetByFilter request) {
        Sort sort = Sort.by(request.getSortBy());

        if(request.getSortDirection().equalsIgnoreCase("DESC")){
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<Transaction> specification = (root, criteria, builder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if(request.getStatus() != null){
                predicates.add(builder.equal(root.get("status"), request.getStatus()));
            }
            if (request.getMinCreatedAt() != null && request.getMaxCreatedAt() != null) {
                predicates.add(builder.between(root.get("createdAt"), request.getMinCreatedAt(), request.getMaxCreatedAt()));
            } 
            predicates.add(builder.between(root.get("totalItems"), request.getMinTotalItems(), request.getMaxTotalItems()));
            predicates.add(builder.between(root.get("grandTotal"), request.getMinGrandTotal(), request.getMaxGrandTotal()));
            predicates.add(builder.between(root.get("totalUnpaid"), request.getMinTotalUnpaid(), request.getMaxTotalUnpaid()));
            predicates.add(builder.between(root.get("totalPaid"), request.getMinTotalPaid(), request.getMaxTotalPaid()));

            return builder.and(predicates);
        }; 

        Slice<Transaction> transactions = transactionRepository.findAll(specification, pageable);

        Slice<TransactionResponse> result = transactions.map(allTransactionMapper::createTransactionResponseDto);

        return result;
    }

    @Override
    public TransactionResponse setTransactionToProcess(UUID id) {
        List<String> messages = new ArrayList<>();

        Transaction searchedTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + id + " was not found"));
        
        if(searchedTransaction.getStatus() == null || searchedTransaction.getStatus() != TransactionStatus.PENDING) throw new ForbiddenRequestException("Unable to process transaction because Transaction Status is not PENDING(CART)");
        List<TransactionItem> transactionItems = searchedTransaction.getTransactionItems(); 

        List<Long> listProductIdFromTrxItems = transactionItems.stream().map(item -> item.getProduct().getId()).distinct().toList();
        List<Product> listProductFromTrxItemsUpdated = productRepository.findAllById(listProductIdFromTrxItems);

        Map<Long, Product> productMap = listProductFromTrxItemsUpdated.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        Set<UUID> listOfOutStockAndRemovedProduct = new HashSet<>();
        transactionItems.forEach(item -> {
            Product updatedProduct = productMap.get(item.getProduct().getId()); 

            if(updatedProduct == null || updatedProduct.getStockQuantity() == 0) {
                listOfOutStockAndRemovedProduct.add(item.getId());
                messages.add("Item removed due to outOfStock or removed Product, Product item ID: " + item.getProduct().getId());
                return;
            }

            item.setPrice(updatedProduct.getSellPrice());

            if(updatedProduct.getStockQuantity() < item.getQuantity()){
                messages.add(updatedProduct.getName() + " stock lesser than " + item.getQuantity() + ", quantity decreased to " + updatedProduct.getStockQuantity());
                item.setQuantity(updatedProduct.getStockQuantity());
            }
            updatedProduct.setStockQuantity(updatedProduct.getStockQuantity()-item.getQuantity());
        }); 

        if (listOfOutStockAndRemovedProduct.size() != 0) {
            transactionItemRepository.deleteAllByIdInBatch(listOfOutStockAndRemovedProduct);
            transactionItems.removeIf(item -> listOfOutStockAndRemovedProduct.contains(item.getId()));
        }
        searchedTransaction.setTotalItems(Long.valueOf(transactionItems.size()));
        searchedTransaction.setSubTotal(transactionItems.stream().mapToLong(item -> item.getPrice() * item.getQuantity()).sum());
        searchedTransaction.setGrandTotal(searchedTransaction.getSubTotal() - searchedTransaction.getTotalDiscount() + searchedTransaction.getTotalFee());
        searchedTransaction.setStatus(TransactionStatus.PROCESS);

        return allTransactionMapper.createTransactionResponseDto(searchedTransaction, messages);
    }

    @Override
    public TransactionResponse setTransactionToComplete(UUID id) {
        Transaction searchedTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + id + " was not found"));
            
        if(searchedTransaction.getStatus() == TransactionStatus.COMPLETE) throw new BoilerplateRequestException("Transaction with ID " + id + " already process");
        if(searchedTransaction.getStatus() != TransactionStatus.PROCESS) throw new ForbiddenRequestException("Transaction with ID " + id + " is " + searchedTransaction.getStatus() + " and can't be set to COMPLETE");

        searchedTransaction.setStatus(TransactionStatus.COMPLETE);

        return allTransactionMapper.createTransactionResponseDto(searchedTransaction);
    }

    @Override
    public TransactionResponse cancelTransaction(UUID id) {
        Transaction searchedTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + id + " was not found"));

        if (searchedTransaction.getStatus() != TransactionStatus.PROCESS && searchedTransaction.getStatus() != TransactionStatus.COMPLETE ) throw new ForbiddenRequestException("Couldn't cancel the transaction because Transaction Status is not PROCESS OR COMPLETE");

        List<TransactionItem> transactionItems = searchedTransaction.getTransactionItems();
        List<Long> listProductIdFromTrxItems = transactionItems.stream().map(item -> item.getProduct().getId()).distinct().toList();
        List<Product> listProductFromTrxItemsUpdated = productRepository.findAllById(listProductIdFromTrxItems);

        Map<Long, Product> productMap = listProductFromTrxItemsUpdated.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        for(TransactionItem item: transactionItems){
            Product product = productMap.get(item.getProduct().getId());
            if(product == null) continue;

            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        }
        searchedTransaction.setTotalUnrefunded(searchedTransaction.getTotalPaid());
        searchedTransaction.setTotalUnpaid(0L);
        searchedTransaction.setTotalPaid(0L); 
        searchedTransaction.setStatus(TransactionStatus.CANCELLED);

        TransactionResponse transactionResponseDto = allTransactionMapper.createTransactionResponseDto(searchedTransaction);
        return transactionResponseDto;
    }

    @Override
    public TransactionResponse getTransaction(UUID id) {
        Transaction searchedTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + id + " was not found"));

        return allTransactionMapper.createTransactionResponseDto(searchedTransaction);
    }

    @Override
    public TransactionResponse refreshTransaction(UUID id) {
        List<String> messages= new ArrayList<>();

        Transaction searchedTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + id + " was not found"));

        if (searchedTransaction.getStatus() != TransactionStatus.PENDING) throw new ForbiddenRequestException("Couldn't delete the item because Transaction Status is not PENDING(CART)");

        List<TransactionItem> transactionItems = searchedTransaction.getTransactionItems();
        List<Long> listProductIdFromTrxItems = transactionItems.stream().map(item -> item.getProduct().getId()).distinct().toList();
        List<ProductRefreshProjection> listProductFromTrxItemsUpdated = productRepository.searchIdUpdatedAtMoreThan(listProductIdFromTrxItems, searchedTransaction.getCreatedAt());

        Map<Long, ProductRefreshProjection>productMap = listProductFromTrxItemsUpdated.stream().collect(Collectors.toMap(ProductRefreshProjection::id, Function.identity()));

        transactionItems.forEach(item -> {
            ProductRefreshProjection updatedProduct = productMap.get(item.getProduct().getId());
            if(updatedProduct == null) return;

            item.setPrice(updatedProduct.sellPrice());

            if(updatedProduct.stockQuantity() < item.getQuantity()){
                messages.add("Product stock lesser than " + item.getQuantity() + ", transaction quantity decreased to " + updatedProduct.stockQuantity());
                item.setQuantity(updatedProduct.stockQuantity());
            }
        });
        
        searchedTransaction.setTotalItems(Long.valueOf(transactionItems.size()));
        searchedTransaction.setSubTotal(transactionItems.stream().mapToLong(item -> item.getPrice() * item.getQuantity()).sum());
        searchedTransaction.setGrandTotal(searchedTransaction.getSubTotal() - searchedTransaction.getTotalDiscount() + searchedTransaction.getTotalFee());

        TransactionResponse transactionResponseDto = allTransactionMapper.createTransactionResponseDto(searchedTransaction, messages);
        return transactionResponseDto;

    }

    @Override
    public byte[] getInvoicePdf(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvoicePdf'");
    }
    
}
