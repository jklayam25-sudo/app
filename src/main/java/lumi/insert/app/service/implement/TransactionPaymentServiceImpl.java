package lumi.insert.app.service.implement;
 
import java.util.UUID;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.core.entity.Customer;
import lumi.insert.app.core.entity.Transaction;
import lumi.insert.app.core.entity.TransactionPayment;
import lumi.insert.app.core.entity.nondatabase.TransactionStatus;
import lumi.insert.app.core.repository.TransactionPaymentRepository;
import lumi.insert.app.core.repository.TransactionRepository;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.TransactionPaymentCreateRequest;
import lumi.insert.app.dto.request.TransactionPaymentGetByFilter;
import lumi.insert.app.dto.response.TransactionPaymentResponse;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;
import lumi.insert.app.mapper.AllTransactionMapper;
import lumi.insert.app.service.TransactionPaymentService;
import lumi.insert.app.utils.generator.JpaSpecGenerator;

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

    @Autowired
    JpaSpecGenerator jpaSpecGenerator;

    @Override
    public TransactionPaymentResponse createTransactionPayment(UUID transactionId, TransactionPaymentCreateRequest request) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + transactionId + " was not found"));

        TransactionPayment transactionPayment = TransactionPayment.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .transaction(transaction)
            .paymentFrom(request.getPaymentFrom())
            .paymentTo(request.getPaymentTo())
            .totalPayment(request.getTotalPayment())
            .build();

        transaction.setTotalUnpaid(transaction.getTotalUnpaid() - request.getTotalPayment());
        transaction.setTotalPaid(transaction.getTotalPaid() + request.getTotalPayment());

        if(transaction.getTotalUnpaid() < 0) throw new TransactionValidationException("Payment exceeds the remaining transaction debts with ID " + transactionId + ", enter an exact amount to proceed");

        Customer customer = transaction.getCustomer();
        customer.setTotalUnpaid(customer.getTotalUnpaid() - request.getTotalPayment());
        customer.setTotalPaid(customer.getTotalPaid() + request.getTotalPayment());
        // Upcoming: integrate with email notification
        if(transaction.getTotalUnpaid() == 0) transaction.setStatus(TransactionStatus.COMPLETE);
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
        Pageable pageable = jpaSpecGenerator.pageable(request);

        Specification<TransactionPayment> specification = jpaSpecGenerator.transactionPaymentSpecification(request);

        Slice<TransactionPayment> transactionPayments = transactionPaymentRepository.findAll(specification, pageable);
        Slice<TransactionPaymentResponse> result = transactionPayments.map(allTransactionMapper::createTransactionPaymentResponseDto);

        return result;
    }

    @Override
    public TransactionPaymentResponse refundTransactionPayment(UUID transactionId, TransactionPaymentCreateRequest request) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new NotFoundEntityException("Transaction with ID " + transactionId + " was not found"));
 
        if (transaction.getStatus() == TransactionStatus.PENDING || transaction.getStatus() == TransactionStatus.COMPLETE ) throw new ForbiddenRequestException("Refund payment only to Transaction with status PROCESS(onGoing) or CANCELLED, check carefully");

        Long totalUnrefunded = transaction.getTotalUnrefunded();

        if(request.getTotalPayment() > totalUnrefunded) throw new TransactionValidationException("Payment refund exceeds the remaining transaction unrefunded debt with ID " + transaction.getId() + ", enter an exact amount to proceed");

        TransactionPayment refundTransactionPayment = TransactionPayment.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .transaction(transaction)
            .paymentFrom(request.getPaymentFrom())
            .paymentTo(request.getPaymentTo())
            .totalPayment(request.getTotalPayment())
            .isForRefund(true)
            .build();

        Customer customer = transaction.getCustomer();
        customer.setTotalRefunded(customer.getTotalRefunded() + request.getTotalPayment());
        customer.setTotalUnrefunded(customer.getTotalUnrefunded() - request.getTotalPayment());

        transaction.setTotalRefunded(transaction.getTotalRefunded() + request.getTotalPayment());
        transaction.setTotalUnrefunded(transaction.getTotalUnrefunded() - request.getTotalPayment());
        
        // Upcoming: integrate with email notification
        if(transaction.getTotalUnrefunded() == 0) transaction.setStatus(TransactionStatus.COMPLETE);
        TransactionPayment savedTransactionPayment = transactionPaymentRepository.save(refundTransactionPayment);
        
        TransactionPaymentResponse transactionPaymentResponseDto = allTransactionMapper.createTransactionPaymentResponseDto(savedTransactionPayment);
        log.info("{}", transactionPaymentResponseDto);
        return transactionPaymentResponseDto;
    }
    
}
