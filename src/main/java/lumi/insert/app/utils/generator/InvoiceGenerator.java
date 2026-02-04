package lumi.insert.app.utils.generator;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
 
import org.springframework.stereotype.Component;
 

@Component
public class InvoiceGenerator { 

    public String generate(){
        return "INV-" + LocalDateTime.now().getNano() + "-" + ThreadLocalRandom.current().nextInt(1000, 10000);
    }
}
