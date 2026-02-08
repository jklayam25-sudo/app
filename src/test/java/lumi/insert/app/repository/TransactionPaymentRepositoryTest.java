package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
 
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional; 
import lumi.insert.app.dto.request.TransactionPaymentGetByFilter;
import lumi.insert.app.entity.Transaction; 
import lumi.insert.app.entity.TransactionPayment; 
import lumi.insert.app.utils.generator.InvoiceGenerator;

@DataJpaTest
@Transactional
@Import(InvoiceGenerator.class)
public class TransactionPaymentRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;
    
    @Autowired
    TransactionPaymentRepository transactionPaymentRepository; 

    @Autowired 
    InvoiceGenerator invoiceGenerator;

    @BeforeEach
    public void setUp(){

    }

    @Test
    @DisplayName("Should add transaction_items entity to database when base field < invoiceId required is valid")
    public void createTransactionPayments_baseField_returnSavedEntity(){
        String invoiceId = invoiceGenerator.generate();

        Transaction transaction = Transaction.builder()
        .invoiceId(invoiceId)
        .build();

       Transaction savedTransaction = transactionRepository.save(transaction);
 
        TransactionPayment transactionPayment = TransactionPayment.builder()
        .transaction(savedTransaction)
        .totalPayment(1000L)
        .paymentFrom("Incart Global Lte - 2432131")
        .paymentTo("PT. Juke Ner - BCA 14123124")
        .build();

        TransactionPayment savedTransactionPayment= transactionPaymentRepository.saveAndFlush(transactionPayment);

        assertNotNull(savedTransactionPayment.getCreatedAt()); 
        assertEquals(savedTransaction.getId(), savedTransactionPayment.getTransaction().getId());
        assertEquals("Incart Global Lte - 2432131", savedTransactionPayment.getPaymentFrom());
    }

    @Test
    @DisplayName("Should return Slice of transaction_items entity")
    public void findAllByTransactionId_validRequest_returnPageableEntity(){
        String invoiceId = invoiceGenerator.generate();

            Transaction transaction = Transaction.builder()
            .invoiceId(invoiceId)
            .build();

            Transaction savedTransaction = transactionRepository.save(transaction);

            
        for (int i = 0; i < 3; i++) {
            TransactionPayment transactionPayment = TransactionPayment.builder()
                .transaction(savedTransaction)
                .totalPayment(1000L * i)
                .paymentFrom("Incart Global Lte - 2432131")
                .paymentTo("PT. Juke Ner - BCA 14123124")
                .build();

            transactionPaymentRepository.saveAndFlush(transactionPayment);
        }
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("createdAt").ascending());
        Slice<TransactionPayment> searchedItem = transactionPaymentRepository.findAllByTransactionId(savedTransaction.getId(), pageable);

        assertEquals(2, searchedItem.getNumberOfElements());
        assertTrue(searchedItem.hasNext());
        assertEquals(1000L, searchedItem.getContent().getLast().getTotalPayment());
    }

    @Test
    @DisplayName("Should return Optional Empty entity when transaction and product id is not valid")
    public void findAllByTransactionId_invalidId_returnOptionalEmptyEntity(){
        Slice<TransactionPayment> searchedItem = transactionPaymentRepository.findAllByTransactionId(UUID.randomUUID(), PageRequest.of(0, 2, Sort.by("createdAt").ascending()));

        assertTrue(searchedItem.isEmpty());
    }

    @Test
    @DisplayName("Should return filtered transaction payment case 1: totalPayment between 1000 - 2000")
    public void findAllCriteria_totalPaymentBetween_returnTotalPayment1000To2000(){
        String invoiceId = invoiceGenerator.generate();

        Transaction transaction = Transaction.builder()
            .invoiceId(invoiceId)
            .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionPayment transactionPaymentSuccess = TransactionPayment.builder()
        .paymentFrom("from")
        .paymentTo("to")
        .totalPayment(1500L)
        .transaction(savedTransaction)
        .build();

        TransactionPayment transactionPaymentFail = TransactionPayment.builder()
        .paymentFrom("from")
        .paymentTo("to")
        .totalPayment(2100L)
        .transaction(savedTransaction)
        .build();

        transactionPaymentRepository.saveAllAndFlush(List.of(transactionPaymentSuccess, transactionPaymentFail));

        TransactionPaymentGetByFilter request = TransactionPaymentGetByFilter.builder()
        .minTotalPayment(1000L)
        .maxTotalPayment(2000L)
        .build();

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
        assertEquals(1, transactionPayments.getNumberOfElements());
        assertEquals(1500L, transactionPayments.getContent().getFirst().getTotalPayment());
    }

    @Test
    @DisplayName("Should return filtered transaction payment case 1: totalPayment between 1000 - 3000 and only from trx A")
    public void findAllCriteria_createdAtBetween_returnEmpty(){
        String invoiceIdA = invoiceGenerator.generate();

        Transaction transactionA = Transaction.builder()
            .invoiceId(invoiceIdA)
            .build();

        Transaction transactionB = Transaction.builder()
            .invoiceId(invoiceGenerator.generate())
            .build();

        List<Transaction> savedTransaction = transactionRepository.saveAllAndFlush(List.of(transactionA, transactionB));

        TransactionPayment transactionPaymentSuccess = TransactionPayment.builder()
        .paymentFrom("from")
        .paymentTo("to")
        .totalPayment(1500L)
        .transaction(savedTransaction.getFirst())
        .build();

        TransactionPayment transactionPaymentFail = TransactionPayment.builder()
        .paymentFrom("from")
        .paymentTo("to")
        .totalPayment(2100L)
        .transaction(savedTransaction.getLast())
        .build();

        transactionPaymentRepository.saveAllAndFlush(List.of(transactionPaymentSuccess, transactionPaymentFail));

        TransactionPaymentGetByFilter request = TransactionPaymentGetByFilter.builder()
        .minTotalPayment(1000L)
        .maxTotalPayment(3000L)
        .transactionId(savedTransaction.getLast().getId())
        .build();

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
        assertEquals(1, transactionPayments.getNumberOfElements());
        assertEquals(2100L, transactionPayments.getContent().getFirst().getTotalPayment());
    }

}
