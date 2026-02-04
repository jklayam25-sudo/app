package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest; 
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import jakarta.transaction.Transactional;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionStatus; 
import lumi.insert.app.utils.generator.InvoiceGenerator;

@DataJpaTest
@Transactional
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
        assertThrows(InvalidDataAccessResourceUsageException.class, () -> transactionRepository.saveAndFlush(transaction));
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

}
