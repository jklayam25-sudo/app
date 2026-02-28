package lumi.insert.app.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Customer;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.StockCard;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.entity.nondatabase.StockMove;
import lumi.insert.app.entity.nondatabase.TransactionStatus;
import lumi.insert.app.repository.CustomerRepository;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.StockCardRepository;
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
    CustomerRepository customerRepository;

    @Autowired
    StockCardRepository stockCardRepository;

    @Autowired
    InvoiceGenerator invoiceGenerator;

    @Autowired
    EntityManager entityManager;

    Customer customer;

    @BeforeEach
    void setup(){ 
        customer = customerRepository.save(Customer.builder().name("TESTES").contact("TESTE123").shippingAddress("SHIPTEST").build());
    }
    
    @Test
    @DisplayName("Should return TransactionResponse DTO when set transaction.status to Process succeed")
    public void setTransactionToProcess_validRequest_returnTransactionResponse(){
        Transaction transaction = Transaction.builder()
            .invoiceId(invoiceGenerator.generate())
            .customer(customer)
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

        Slice<StockCard> stockcards = stockCardRepository.findAllByReferenceId(savedTransaction.getTransactionItems().getLast().getId());
        assertEquals(1, stockcards.getNumberOfElements());
        assertEquals(14L, stockcards.getContent().getLast().getOldStock());
        assertEquals(-5L, stockcards.getContent().getLast().getQuantity());
        assertEquals(StockMove.SALE, stockcards.getContent().getLast().getType());
        assertEquals(9L, stockcards.getContent().getLast().getNewStock());
        assertEquals("Product-4", stockcards.getContent().getLast().getProductName());

        product3 = productRepository.findById(idProduct3).orElseThrow();
        assertEquals(0, product3.getStockQuantity());
    }

    @Test
    @DisplayName("Should calculate and refresh the items, return TransactionResponse DTO when succeed")
    public void refreshTransaction_validRequest_returnTransactionResponse(){
        Transaction transaction = Transaction.builder()
            .invoiceId(invoiceGenerator.generate())
            .customer(customer)
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
            .customer(customer)
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

        Slice<StockCard> stockcards = stockCardRepository.findAllByReferenceId(savedTransaction.getTransactionItems().getLast().getId());
        assertEquals(1, stockcards.getNumberOfElements());
        assertEquals(14L, stockcards.getContent().getLast().getOldStock());
        assertEquals(5L, stockcards.getContent().getLast().getQuantity());
        assertEquals(StockMove.CUSTOMER_IN, stockcards.getContent().getLast().getType());
        assertEquals(19L, stockcards.getContent().getLast().getNewStock());
        assertEquals("Product-4", stockcards.getContent().getLast().getProductName());

        product3 = productRepository.findById(idProduct3).orElseThrow();
        assertEquals(4, product3.getStockQuantity());
    }

    @Test
    @DisplayName("Should calculate and cancel the items, return TransactionResponse DTO when succeed. CASE : CUSTOMER PAID, REFUND 1 ITEM (OUR DEBT) THEN CANCEL")
    public void cancelTransaction_validRequestCase_returnTransactionResponse(){
        Transaction transaction = Transaction.builder()
            .invoiceId(invoiceGenerator.generate())
            .status(TransactionStatus.PROCESS)
            .totalPaid(4000L)
            .totalUnrefunded(100L)
            .customer(customer)
            .build();

        Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);
        Long idProduct3 = null;
        UUID idItems3 = null;
        for (int i = 1; i < 6; i++) {
            Product product = Product.builder()
                .name("Product-" + i)
                .basePrice(900L * i)
                .sellPrice(1000L * i)
                .stockQuantity(10L + i)
                .build();

            Product savedProduct = productRepository.saveAndFlush(product);
            
            TransactionItem transactionItem = TransactionItem.builder()
                .price(savedProduct.getSellPrice())
                .quantity(1L + i)
                .description(savedProduct.getName())
                .transaction(savedTransaction)
                .product(savedProduct)
                .build();

            TransactionItem savedTransactionItem = transactionItemRepository.saveAndFlush(transactionItem);
            if(i == 3) {
                idProduct3 = savedProduct.getId(); 
                idItems3 = savedTransactionItem.getId();
            }

            savedTransaction.getTransactionItems().add(savedTransactionItem);

        }
        Product product3 = productRepository.findById(idProduct3).orElseThrow();

        TransactionItem transactionItem = TransactionItem.builder()
                .price(product3.getSellPrice())
                .quantity(-2L)
                .description(product3.getName())
                .transaction(savedTransaction)
                .product(product3)
                .build();

        TransactionItem savedTransactionItem = transactionItemRepository.saveAndFlush(transactionItem);
        savedTransaction.getTransactionItems().add(savedTransactionItem);
 
        product3.setSellPrice(1000L);
        product3.setStockQuantity(0L);                     

        entityManager.flush();
        entityManager.clear();;
        TransactionResponse setTransactionToProcess = transactionService.cancelTransaction(savedTransaction.getId()); 
        assertEquals(0, setTransactionToProcess.totalPaid());
        assertEquals(4000, setTransactionToProcess.totalUnrefunded());
        assertEquals(TransactionStatus.CANCELLED, setTransactionToProcess.status()); 

        Slice<StockCard> stockcards = stockCardRepository.findAllByReferenceId(idItems3);
        assertEquals(1, stockcards.getNumberOfElements());
        assertEquals(0L, stockcards.getContent().getLast().getOldStock());
        assertEquals(2L, stockcards.getContent().getLast().getQuantity());
        assertEquals(StockMove.CUSTOMER_IN, stockcards.getContent().getLast().getType());
        assertEquals(2L, stockcards.getContent().getLast().getNewStock());
        assertEquals("Product-3", stockcards.getContent().getLast().getProductName());

        product3 = productRepository.findById(idProduct3).orElseThrow();
        assertEquals(2, product3.getStockQuantity());

        assertEquals(11, transactionItemRepository.count());
    }

}
