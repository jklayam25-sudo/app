package lumi.insert.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lumi.insert.app.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    
    Optional<Transaction> findByInvoiceId(String invoiceId);

};
