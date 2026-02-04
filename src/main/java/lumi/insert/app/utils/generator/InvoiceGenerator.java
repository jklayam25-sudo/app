package lumi.insert.app.utils.generator;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lumi.insert.app.repository.TransactionRepository;

@Component
public class InvoiceGenerator {
    
    @Autowired
    TransactionRepository transactionRepository;

    public String generate(){
        return "INV-" + LocalDateTime.now().getNano() + "-" + ThreadLocalRandom.current().nextInt(1000, 10000);
    }
}
