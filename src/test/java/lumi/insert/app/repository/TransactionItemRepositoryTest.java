package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import jakarta.transaction.Transactional;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem; 
import lumi.insert.app.utils.forTesting.ProductUtils;
import lumi.insert.app.utils.generator.InvoiceGenerator;

@DataJpaTest
@Transactional
@Import(InvoiceGenerator.class)
public class TransactionItemRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;
    
    @Autowired
    TransactionItemRepository transactionItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired 
    InvoiceGenerator invoiceGenerator;

    @BeforeEach
    public void setUp(){

    }

    @Test
    @DisplayName("Should add transaction_items entity to database when base field < invoiceId required is valid")
    public void createTransactionItems_baseField_returnSavedEntity(){
        String invoiceId = invoiceGenerator.generate();

        Transaction transaction = Transaction.builder()
        .invoiceId(invoiceId)
        .build();

       Transaction savedTransaction = transactionRepository.save(transaction);

       Product mockCategorizedProduct = ProductUtils.getMockCategorizedProduct();
       mockCategorizedProduct.setCategory(null);
       mockCategorizedProduct.setId(null);

       Product savedProduct = productRepository.save(mockCategorizedProduct);

       TransactionItem transactionItem = TransactionItem.builder()
       .transaction(savedTransaction)
       .product(savedProduct)
       .price(savedProduct.getBasePrice())
       .quantity(savedProduct.getStockQuantity())
       .build();

        TransactionItem saveAndFlushTrxItems = transactionItemRepository.saveAndFlush(transactionItem);

        assertNotNull(saveAndFlushTrxItems.getCreatedAt());
        assertEquals(savedProduct.getId(), saveAndFlushTrxItems.getProduct().getId());
        assertEquals(savedTransaction.getId(), saveAndFlushTrxItems.getTransaction().getId());
    }

    @Test
    @DisplayName("Should return transaction_items entity when transaction and product id is valid")
    public void findByTransactionIdAndProductId_validId_returnEntity(){
        String invoiceId = invoiceGenerator.generate();

        Transaction transaction = Transaction.builder()
        .invoiceId(invoiceId)
        .build();

       Transaction savedTransaction = transactionRepository.save(transaction);

       Product mockCategorizedProduct = ProductUtils.getMockCategorizedProduct();
       mockCategorizedProduct.setCategory(null);
       mockCategorizedProduct.setId(null);

       Product savedProduct = productRepository.save(mockCategorizedProduct);

       TransactionItem transactionItem = TransactionItem.builder()
       .transaction(savedTransaction)
       .product(savedProduct)
       .price(savedProduct.getBasePrice())
       .quantity(savedProduct.getStockQuantity())
       .build();

        transactionItemRepository.saveAndFlush(transactionItem);

        TransactionItem searchedItem = transactionItemRepository.findByTransactionIdAndProductId(savedTransaction.getId(), savedProduct.getId()).orElseThrow();

        assertEquals(savedProduct.getId(), searchedItem.getProduct().getId());
        assertEquals(savedTransaction.getId(), searchedItem.getTransaction().getId());
    }

    @Test
    @DisplayName("Should return Optional Empty entity when transaction and product id is not valid")
    public void findByTransactionIdAndProductId_invalidId_returnOptionalEmptyEntity(){
        Optional<TransactionItem> searchedItem = transactionItemRepository.findByTransactionIdAndProductId(UUID.randomUUID(), 15L);

        assertTrue(searchedItem.isEmpty());
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
            Product mockCategorizedProduct = ProductUtils.getMockCategorizedProduct();
            mockCategorizedProduct.setCategory(null);
            mockCategorizedProduct.setId(null);
            mockCategorizedProduct.setName(mockCategorizedProduct.getName() + i);
            Product savedProduct = productRepository.save(mockCategorizedProduct);

            TransactionItem transactionItem = TransactionItem.builder()
            .transaction(savedTransaction)
            .product(savedProduct)
            .price(savedProduct.getBasePrice())
            .quantity(savedProduct.getStockQuantity())
            .build();

            transactionItemRepository.saveAndFlush(transactionItem);
        }
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("createdAt").ascending());
        Slice<TransactionItem> searchedItem = transactionItemRepository.findAllByTransactionId(savedTransaction.getId(), pageable);

        assertEquals(2, searchedItem.getNumberOfElements());
        assertTrue(searchedItem.hasNext());
        assertEquals("Product1", searchedItem.getContent().getLast().getProduct().getName());
    }

    @Test
    @DisplayName("Should return Optional Empty entity when transaction and product id is not valid")
    public void findAllByTransactionId_invalidId_returnOptionalEmptyEntity(){
        Slice<TransactionItem> searchedItem = transactionItemRepository.findAllByTransactionId(UUID.randomUUID(), PageRequest.of(0, 2, Sort.by("createdAt").ascending()));

        assertTrue(searchedItem.isEmpty());
    }

}
