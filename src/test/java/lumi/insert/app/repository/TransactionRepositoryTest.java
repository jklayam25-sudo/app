package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException; 
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.dto.request.TransactionGetByFilter;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.nondatabase.TransactionStatus;
import lumi.insert.app.utils.generator.InvoiceGenerator;

@DataJpaTest
@Transactional
@Slf4j
public class TransactionRepositoryTest {
    
    @Autowired
    TransactionRepository transactionRepository;

    InvoiceGenerator invoiceGenerator = new InvoiceGenerator();

    @Test
    @DisplayName("Should add transaction entity to database when base field < invoiceId required is valid")
    public void createTransaction_baseField_returnSavedEntity(){
        String invoiceId = invoiceGenerator.generate();

        Transaction transaction = Transaction.builder()
        .invoiceId(invoiceId)
        .build();

      Transaction savedTransaction = transactionRepository.save(transaction);

      assertEquals(0, savedTransaction.getGrandTotal());
      assertEquals(TransactionStatus.PENDING, savedTransaction.getStatus());
      assertEquals(invoiceId, savedTransaction.getInvoiceId());
      assertNotNull(savedTransaction.getId());
    }

    @Test
    @DisplayName("Should thrown jpa data error when base field < invoiceId is null")
    public void createTransaction_nullableField_throwError(){
        Transaction transaction = Transaction.builder() 
        .build();
        
        assertNull(transaction.getInvoiceId());
        assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.saveAndFlush(transaction));
    }

    @Test
    @DisplayName("Should return trx entity when invoiceId is valid")
    public void findByInvoiceId_validId_returnEntity(){
        String invoiceId = invoiceGenerator.generate();

        Transaction transaction = Transaction.builder()
        .invoiceId(invoiceId)
        .build();

        transactionRepository.saveAndFlush(transaction);

        Transaction searchedTransaction = transactionRepository.findByInvoiceId(invoiceId).orElseThrow();

        assertEquals(invoiceId, searchedTransaction.getInvoiceId());
        assertNotNull(searchedTransaction.getCreatedAt());
        assertNotNull(searchedTransaction.getId());
    }

    @Test
    @DisplayName("Should return optional of empty when invoiceId is not valid")
    public void findByInvoiceId_invalidId_returnOptionalEmptyEntity(){

        assertThrows(NoSuchElementException.class, () -> transactionRepository.findByInvoiceId("S").orElseThrow());

    }

    @Test
    @DisplayName("Should return filtered transaction case 1: status.CANCELLED")
    public void findAllCriteria_statusCancelled_returnCancelledTransaction(){
        Set<Transaction> pendingTransactions = new HashSet<>();
        
        for (int i = 1; i < 3; i++) {
            if(i%2 == 0) {
                Transaction transaction = Transaction.builder()
                    .invoiceId(invoiceGenerator.generate())
                    .status(TransactionStatus.CANCELLED)
                    .build();

                 pendingTransactions.add(transaction);
            } else {
                Transaction transaction = Transaction.builder()
                    .invoiceId(invoiceGenerator.generate())
                    .build();

                 pendingTransactions.add(transaction);
            }
        }

        transactionRepository.saveAllAndFlush(pendingTransactions);

        TransactionGetByFilter request = TransactionGetByFilter.builder()
        .status(TransactionStatus.CANCELLED)
        .build();

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
        assertEquals(1, transactions.getNumberOfElements());
        assertEquals(TransactionStatus.CANCELLED, transactions.getContent().getFirst().getStatus());

    }

    @Test
    @DisplayName("Should return filtered transaction case 2: combine: status.PENDING && totalPaid between 100-200 && createdAt betwenn 2020-2021")
    public void findAllCriteria_combineFilter_returnCombinedFilter(){
        String matchInvoiceId = invoiceGenerator.generate();

        Transaction matchTransaction = Transaction.builder()
        .invoiceId(matchInvoiceId)
        .totalPaid(150L)
        .build();

        matchTransaction.setCreatedAt(LocalDateTime.of(2020, 5, 10, 10, 10));

        Transaction unmatchTransaction2 = Transaction.builder()
        .invoiceId(invoiceGenerator.generate())
        .totalPaid(350L)
        .build();

        matchTransaction.setCreatedAt(LocalDateTime.of(2020, 5, 10, 10, 10));

        Transaction unmatchTransaction3 = Transaction.builder()
        .invoiceId(invoiceGenerator.generate())
        .status(TransactionStatus.CANCELLED)
        .totalPaid(150L)
        .build();

        matchTransaction.setCreatedAt(LocalDateTime.of(2020, 5, 10, 10, 10));

        transactionRepository.saveAllAndFlush(List.of(matchTransaction, unmatchTransaction2, unmatchTransaction3));

        TransactionGetByFilter request = TransactionGetByFilter.builder()
        .status(TransactionStatus.PENDING)
        .minTotalPaid(100L)
        .maxTotalPaid(200L)
        .minCreatedAt(LocalDateTime.of(2020, 1, 10, 10, 10))
        .maxCreatedAt(LocalDateTime.of(2027, 1, 10, 10, 10))
        .build();

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
        log.info("haleak, {}" + transactions.getContent().getFirst().getCreatedAt());
        assertEquals(1, transactions.getNumberOfElements());
        assertEquals(TransactionStatus.PENDING, transactions.getContent().getFirst().getStatus());
        assertEquals(matchInvoiceId, transactions.getContent().getFirst().getInvoiceId());
    }


}
