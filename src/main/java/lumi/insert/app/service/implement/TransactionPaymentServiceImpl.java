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
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.TransactionPaymentCreateRequest;
import lumi.insert.app.dto.request.TransactionPaymentGetByFilter;
import lumi.insert.app.dto.response.TransactionPaymentResponse;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionPayment;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;
import lumi.insert.app.repository.TransactionPaymentRepository;
import lumi.insert.app.repository.TransactionRepository;
import lumi.insert.app.service.TransactionPaymentService;
import lumi.insert.app.utils.mapper.AllTransactionMapper;

@Service
@Transactional
@Slf4j
public class TransactionPaymentServiceImpl implements TransactionPaymentService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionPaymentRepository transactionPaymentRepository;

    @Autowired
    AllTransactionMapper allTransactionMapper;

    @Override
    public TransactionPaymentResponse createTransactionPayment(UUID transactionId, TransactionPaymentCreateRequest request) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + transactionId + " was not found"));

        TransactionPayment transactionPayment = TransactionPayment.builder()
        .transaction(transaction)
        .paymentFrom(request.getPaymentFrom())
        .paymentTo(request.getPaymentTo())
        .totalPayment(request.getTotalPayment())
        .build();

        transaction.setTotalUnpaid(transaction.getTotalUnpaid() - request.getTotalPayment());
        transaction.setTotalPaid(transaction.getTotalPaid() + request.getTotalPayment());

        if(transaction.getTotalUnpaid() < 0) throw new TransactionValidationException("Payment exceeds the remaining transaction debts with ID " + transactionId + ", enter an exact amount to proceed");

        TransactionPayment savedTransactionPayment = transactionPaymentRepository.save(transactionPayment);
        TransactionPaymentResponse transactionPaymentResponseDto = allTransactionMapper.createTransactionPaymentResponseDto(savedTransactionPayment);

        return transactionPaymentResponseDto;
    }

    @Override
    public Slice<TransactionPaymentResponse> getTransactionPaymentsByTransactionId(UUID transactionId, PaginationRequest request) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()).withSort(sort);

        Slice<TransactionPayment> transactionPayments = transactionPaymentRepository.findAllByTransactionId(transactionId, pageable);
        Slice<TransactionPaymentResponse> result = transactionPayments.map(allTransactionMapper::createTransactionPaymentResponseDto);

        return result;
    }

    @Override
    public TransactionPaymentResponse getTransactionPayment(UUID id) {
        TransactionPayment transactionPayment = transactionPaymentRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Transaction Payment with ID " + id + " was not found"));
        
        TransactionPaymentResponse transactionPaymentResponseDto = allTransactionMapper.createTransactionPaymentResponseDto(transactionPayment);
        return transactionPaymentResponseDto;
    }

    @Override
    public Slice<TransactionPaymentResponse> getTransactionPaymentsByRequests(TransactionPaymentGetByFilter request) {
        Sort sort = Sort.by(request.getSortBy());

        if(request.getSortDirection().equalsIgnoreCase("DESC")){
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()).withSort(sort);

        Specification<TransactionPayment> specification = (root, criteria, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(request.getTransactionId() != null) predicates.add(builder.equal(root.get("transaction").get("id"), request.getTransactionId()));
            if(request.getMinCreatedAt() != null && request.getMaxCreatedAt() != null) predicates.add(builder.between(root.get("createdAt"), request.getMinCreatedAt(), request.getMaxCreatedAt()));

            predicates.add(builder.between(root.get("totalPayment"), request.getMinTotalPayment(), request.getMaxTotalPayment()));

            return builder.and(predicates);
        };

        Slice<TransactionPayment> transactionPayments = transactionPaymentRepository.findAll(specification, pageable);
        Slice<TransactionPaymentResponse> result = transactionPayments.map(allTransactionMapper::createTransactionPaymentResponseDto);

        return result;
    }
    
}
