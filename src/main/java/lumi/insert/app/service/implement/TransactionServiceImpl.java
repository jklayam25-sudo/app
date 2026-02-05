package lumi.insert.app.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionStatus;
import lumi.insert.app.exception.BoilerplateRequestException;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.repository.TransactionRepository;
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
        Transaction searchedTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + id + " was not found"));
        
        if(searchedTransaction.getStatus() == TransactionStatus.PROCESS) throw new BoilerplateRequestException("Transaction with ID " + id + " already process");
        if(searchedTransaction.getStatus() != TransactionStatus.PENDING) throw new ForbiddenRequestException("Transaction with ID " + id + " is " + searchedTransaction.getStatus() + " and can't be set to PROCESS");
        
        searchedTransaction.setStatus(TransactionStatus.PROCESS);

        return allTransactionMapper.createTransactionResponseDto(searchedTransaction);
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelTransaction'");
    }

    @Override
    public TransactionResponse getTransaction(UUID id) {
        Transaction searchedTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + id + " was not found"));

        return allTransactionMapper.createTransactionResponseDto(searchedTransaction);
    }

    @Override
    public TransactionResponse refreshTransaction(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshTransaction'");
    }

    @Override
    public byte[] getInvoicePdf(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvoicePdf'");
    }
    
}
