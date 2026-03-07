package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime; 
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;  
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice; 
import org.springframework.data.jpa.domain.Specification;

import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.dto.request.SupplyGetByFilter;
import lumi.insert.app.entity.Supplier;
import lumi.insert.app.entity.Supply;
import lumi.insert.app.entity.nondatabase.SupplyStatus;
import lumi.insert.app.utils.generator.InvoiceGenerator;
import lumi.insert.app.utils.generator.JpaSpecGenerator;

@DataJpaTest
@Transactional
@Slf4j
@Import(JpaSpecGenerator.class)
public class SupplyRepositoryTest {
    
    @Autowired
    SupplyRepository supplyRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    JpaSpecGenerator jpaSpecGenerator;

    InvoiceGenerator invoiceGenerator = new InvoiceGenerator();

    Supplier setupSupplier;

    @BeforeEach
    public void setup(){
        Supplier supplier1 = Supplier.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .name("Supplier 1")
        .email("TEST@mail.com")
        .contact("081234567890") 
        .build();

        setupSupplier = supplierRepository.saveAndFlush(supplier1);
    }

    @Test
    @DisplayName("Should add supply entity to database when base field < invoiceId required is valid")
    public void createSupply_baseField_returnSavedEntity(){
        String invoiceId = invoiceGenerator.generate();

        Supply supply = Supply.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .invoiceId(invoiceId)
        .supplier(setupSupplier)
        .build();

        Supply savedSupply = supplyRepository.save(supply);

        assertEquals(0, savedSupply.getGrandTotal());
        assertEquals(SupplyStatus.UNPAID, savedSupply.getStatus());
        assertEquals(invoiceId, savedSupply.getInvoiceId());
        assertNotNull(savedSupply.getId());
    }

    @Test
    @DisplayName("Should thrown jpa data error when base field < invoiceId is null")
    public void createSupply_nullableField_throwError(){
        Supply supply = Supply.builder() 
        .id(UuidCreator.getTimeOrderedEpochFast())
        .supplier(setupSupplier)
        .build();
        
        assertNull(supply.getInvoiceId());
        assertThrows(DataIntegrityViolationException.class, () -> supplyRepository.saveAndFlush(supply));
    }

    @Test
    @DisplayName("Should return trx entity when invoiceId is valid")
    public void findByInvoiceId_validId_returnEntity(){
        String invoiceId = invoiceGenerator.generate();

        Supply supply = Supply.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .invoiceId(invoiceId)
        .supplier(setupSupplier)
        .build();

        supplyRepository.saveAndFlush(supply);

        Supply searchedSupply = supplyRepository.findByInvoiceId(invoiceId).orElseThrow();

        assertEquals(invoiceId, searchedSupply.getInvoiceId());
        assertNotNull(searchedSupply.getCreatedAt());
        assertNotNull(searchedSupply.getId());
    }

    @Test
    @DisplayName("Should return optional of empty when invoiceId is not valid")
    public void findByInvoiceId_invalidId_returnOptionalEmptyEntity(){

        assertThrows(NoSuchElementException.class, () -> supplyRepository.findByInvoiceId("S").orElseThrow());

    }

    @Test
    @DisplayName("Should return filtered supply case 1: status.CANCELLED")
    public void findAllCriteria_statusCancelled_returnCancelledSupply(){
        Set<Supply> pendingSupplys = new HashSet<>();
        
        for (int i = 1; i < 3; i++) {
            if(i%2 == 0) {
                Supply supply = Supply.builder()
                    .id(UuidCreator.getTimeOrderedEpochFast())
                    .invoiceId(invoiceGenerator.generate())
                    .status(SupplyStatus.CANCELLED)
                    .supplier(setupSupplier)
                    .build();

                 pendingSupplys.add(supply);
            } else {
                Supply supply = Supply.builder()
                    .id(UuidCreator.getTimeOrderedEpochFast())
                    .invoiceId(invoiceGenerator.generate())
                    .supplier(setupSupplier)
                    .build();

                 pendingSupplys.add(supply);
            }
        }

        supplyRepository.saveAllAndFlush(pendingSupplys);

        SupplyGetByFilter request = SupplyGetByFilter.builder()
        .status(SupplyStatus.CANCELLED)
        .build();

        Pageable pageable = jpaSpecGenerator.pageable(request);

        Specification<Supply> specification = jpaSpecGenerator.supplySpecification(request);
         
        Slice<Supply> supplies = supplyRepository.findAll(specification, pageable);
        assertEquals(1, supplies.getNumberOfElements());
        assertEquals(SupplyStatus.CANCELLED, supplies.getContent().getFirst().getStatus());

    }

    @Test
    @DisplayName("Should return filtered supply case 2: combine: status.PENDING && totalPaid between 100-200 && createdAt betwenn 2020-2021")
    public void findAllCriteria_combineFilter_returnCombinedFilter(){
        String matchInvoiceId = invoiceGenerator.generate();

        Supply matchSupply = Supply.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .invoiceId(matchInvoiceId)
        .totalPaid(150L)
        .supplier(setupSupplier)
        .build();

        matchSupply.setCreatedAt(LocalDateTime.of(2020, 5, 10, 10, 10));

        Supply unmatchSupply2 = Supply.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .invoiceId(invoiceGenerator.generate())
        .totalPaid(350L)
        .supplier(setupSupplier)
        .build();

        matchSupply.setCreatedAt(LocalDateTime.of(2020, 5, 10, 10, 10));

        Supply unmatchSupply3 = Supply.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .invoiceId(invoiceGenerator.generate())
        .status(SupplyStatus.CANCELLED)
        .totalPaid(150L)
        .supplier(setupSupplier)
        .build();

        matchSupply.setCreatedAt(LocalDateTime.of(2020, 5, 10, 10, 10));

        supplyRepository.saveAllAndFlush(List.of(matchSupply, unmatchSupply2, unmatchSupply3));

        SupplyGetByFilter request = SupplyGetByFilter.builder()
        .status(SupplyStatus.UNPAID)
        .minTotalPaid(100L)
        .maxTotalPaid(200L)
        .minCreatedAt(LocalDateTime.of(2020, 1, 10, 10, 10))
        .maxCreatedAt(LocalDateTime.of(2027, 1, 10, 10, 10))
        .build();

        Pageable pageable = jpaSpecGenerator.pageable(request);

        Specification<Supply> specification = jpaSpecGenerator.supplySpecification(request);

        Slice<Supply> supplies = supplyRepository.findAll(specification, pageable);
        log.info("haleak, {}" + supplies.getContent().getFirst().getCreatedAt());
        assertEquals(1, supplies.getNumberOfElements());
        assertEquals(SupplyStatus.UNPAID, supplies.getContent().getFirst().getStatus());
        assertEquals(matchInvoiceId, supplies.getContent().getFirst().getInvoiceId());
    }

    @Test
    @DisplayName("Should return filtered supply case 3: get only supplier 1")
    public void findAllCriteria_supplierCase_returnCombinedFilter(){
        String matchInvoiceId = invoiceGenerator.generate();
 
        Supplier supplier2 = Supplier.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .name("Supplier 2")
        .email("TEST1@mail.com")
        .contact("0812314567890") 
        .build();

        Supplier savedSupplier2 = supplierRepository.saveAndFlush(supplier2);;

        Supply matchSupply = Supply.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .invoiceId(matchInvoiceId)
        .totalPaid(150L)
        .supplier(setupSupplier)
        .build();

        matchSupply.setCreatedAt(LocalDateTime.of(2020, 5, 10, 10, 10));

        Supply unmatchSupply2 = Supply.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .invoiceId(invoiceGenerator.generate())
        .totalPaid(350L)
        .supplier(savedSupplier2)
        .build();

        supplyRepository.saveAllAndFlush(List.of(matchSupply, unmatchSupply2));

        SupplyGetByFilter request = SupplyGetByFilter.builder()
        .supplierId(setupSupplier.getId())
        .build();

        Pageable pageable = jpaSpecGenerator.pageable(request);

        Specification<Supply> specification = jpaSpecGenerator.supplySpecification(request);

        Slice<Supply> supplies = supplyRepository.findAll(specification, pageable); 
        assertEquals(1, supplies.getNumberOfElements());
        assertEquals(setupSupplier.getId(), supplies.getContent().getFirst().getSupplier().getId()); 
    }


}
