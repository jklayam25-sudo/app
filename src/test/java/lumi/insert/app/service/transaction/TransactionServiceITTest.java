package lumi.insert.app.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.entity.nondatabase.TransactionStatus;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.TransactionItemRepository;
import lumi.insert.app.repository.TransactionRepository;
import lumi.insert.app.service.TransactionService;
import lumi.insert.app.utils.generator.InvoiceGenerator;

@SpringBootTest
@Transactional
public class TransactionServiceITTest {
    
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TransactionItemRepository transactionItemRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    InvoiceGenerator invoiceGenerator;

    @Autowired
    EntityManager entityManager;
    
    @Test
    @DisplayName("Should return TransactionResponse DTO when set transaction.status to Process succeed")
    public void setTransactionToProcess_validRequest_returnTransactionResponse(){
        Transaction transaction = Transaction.builder()
            .invoiceId(invoiceGenerator.generate())
            .build();

        Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);
        Long idProduct3 = null;

        for (int i = 1; i < 5; i++) {
            Product product = Product.builder()
                .name("Product-" + i)
                .basePrice(900L * i)
                .sellPrice(1000L * i)
                .stockQuantity(10L + i)
                .build();

            Product savedProduct = productRepository.saveAndFlush(product);
            if(i == 3) idProduct3 = savedProduct.getId();

    
            TransactionItem transactionItem = TransactionItem.builder()
                .price(savedProduct.getSellPrice())
                .quantity(1L + i)
                .description(savedProduct.getName())
                .transaction(savedTransaction)
                .product(savedProduct)
                .build();

            
            TransactionItem savedTransactionItem = transactionItemRepository.saveAndFlush(transactionItem);
            savedTransaction.getTransactionItems().add(savedTransactionItem);

        }
        Product product3 = productRepository.findById(idProduct3).orElseThrow();
        product3.setSellPrice(1000L);
        product3.setStockQuantity(0L);

        entityManager.flush();
        entityManager.clear();;
        TransactionResponse setTransactionToProcess = transactionService.setTransactionToProcess(savedTransaction.getId());
        assertEquals(3, setTransactionToProcess.totalItems());
        assertEquals(28000L, setTransactionToProcess.grandTotal());
        assertEquals(TransactionStatus.PROCESS, setTransactionToProcess.status());
        assertEquals("Item removed due to outOfStock or removed Product, Product item ID: " + product3.getId(), setTransactionToProcess.messages().getFirst());

        product3 = productRepository.findById(idProduct3).orElseThrow();
        assertEquals(0, product3.getStockQuantity());
    }

    @Test
    @DisplayName("Should calculate and refresh the items, return TransactionResponse DTO when succeed")
    public void refreshTransaction_validRequest_returnTransactionResponse(){
        Transaction transaction = Transaction.builder()
            .invoiceId(invoiceGenerator.generate())
            .build();

        Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);
        Long idProduct3 = null;

        for (int i = 1; i < 5; i++) {
            Product product = Product.builder()
                .name("Product-" + i)
                .basePrice(900L * i)
                .sellPrice(1000L * i)
                .stockQuantity(10L + i)
                .build();

            Product savedProduct = productRepository.saveAndFlush(product);
            if(i == 3) idProduct3 = savedProduct.getId();

    
            TransactionItem transactionItem = TransactionItem.builder()
                .price(savedProduct.getSellPrice())
                .quantity(1L + i)
                .description(savedProduct.getName())
                .transaction(savedTransaction)
                .product(savedProduct)
                .build();

            TransactionItem savedTransactionItem = transactionItemRepository.saveAndFlush(transactionItem);
            savedTransaction.getTransactionItems().add(savedTransactionItem);

        }
        Product product3 = productRepository.findById(idProduct3).orElseThrow();
        product3.setSellPrice(1000L);
        product3.setStockQuantity(0L);                     

        entityManager.flush();
        entityManager.clear();;
        TransactionResponse setTransactionToProcess = transactionService.refreshTransaction(savedTransaction.getId());
        assertEquals(4                                                                                                                                                                                                                                                                     , setTransactionToProcess.totalItems());
        assertEquals(28000L, setTransactionToProcess.grandTotal());
        assertEquals(TransactionStatus.PENDING, setTransactionToProcess.status());
        assertNotNull(setTransactionToProcess.messages().getFirst());

        product3 = productRepository.findById(idProduct3).orElseThrow();
        assertEquals(0, product3.getStockQuantity());
    }

    @Test
    @DisplayName("Should calculate and cancel the items, return TransactionResponse DTO when succeed")
    public void cancelTransaction_validRequest_returnTransactionResponse(){
        Transaction transaction = Transaction.builder()
            .invoiceId(invoiceGenerator.generate())
            .status(TransactionStatus.PROCESS)
            .totalPaid(4000L)
            .build();

        Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);
        Long idProduct3 = null;

        for (int i = 1; i < 5; i++) {
            Product product = Product.builder()
                .name("Product-" + i)
                .basePrice(900L * i)
                .sellPrice(1000L * i)
                .stockQuantity(10L + i)
                .build();

            Product savedProduct = productRepository.saveAndFlush(product);
            if(i == 3) idProduct3 = savedProduct.getId();

    
            TransactionItem transactionItem = TransactionItem.builder()
                .price(savedProduct.getSellPrice())
                .quantity(1L + i)
                .description(savedProduct.getName())
                .transaction(savedTransaction)
                .product(savedProduct)
                .build();

            TransactionItem savedTransactionItem = transactionItemRepository.saveAndFlush(transactionItem);
            savedTransaction.getTransactionItems().add(savedTransactionItem);

        }
        Product product3 = productRepository.findById(idProduct3).orElseThrow();
        product3.setSellPrice(1000L);
        product3.setStockQuantity(0L);                     

        entityManager.flush();
        entityManager.clear();;
        TransactionResponse setTransactionToProcess = transactionService.cancelTransaction(savedTransaction.getId()); 
        assertEquals(0, setTransactionToProcess.totalPaid());
        assertEquals(4000, setTransactionToProcess.totalUnrefunded());
        assertEquals(TransactionStatus.CANCELLED, setTransactionToProcess.status()); 

        product3 = productRepository.findById(idProduct3).orElseThrow();
        assertEquals(4, product3.getStockQuantity());
    }

}
