package lumi.insert.app.service.supplier;
 
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import lumi.insert.app.entity.Supplier; 
import lumi.insert.app.repository.SupplierRepository;
import lumi.insert.app.service.implement.SupplierServiceImpl;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.SupplierMapperImpl; 

@ExtendWith(MockitoExtension.class)
public abstract class BaseSupplierServiceTest {

    @InjectMocks
    SupplierServiceImpl supplierServiceMock;

    @Mock
    SupplierRepository supplierRepository;
    
    @Spy
    SupplierMapperImpl supplierMapperImpl = new SupplierMapperImpl();

    @Spy
    JpaSpecGenerator jpaSpecGenerator = new JpaSpecGenerator();

    Supplier setupSupplier;
  
    @BeforeEach
    void setup(){
        setupSupplier = Supplier.builder()
        .id(UUID.randomUUID())
        .name("Test Lte.")
        .contact("Telegram - @Test")  
        .build();
    }
}