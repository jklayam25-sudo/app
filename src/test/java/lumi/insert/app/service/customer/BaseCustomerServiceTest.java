package lumi.insert.app.service.customer;
 
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import lumi.insert.app.entity.Customer; 
import lumi.insert.app.repository.CustomerRepository;
import lumi.insert.app.service.implement.CustomerServiceImpl;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.CustomerMapperImpl; 

@ExtendWith(MockitoExtension.class)
public abstract class BaseCustomerServiceTest {

    @InjectMocks
    CustomerServiceImpl customerServiceMock;

    @Mock
    CustomerRepository customerRepository;
    
    @Spy
    CustomerMapperImpl customerMapperImpl = new CustomerMapperImpl();

    @Spy
    JpaSpecGenerator jpaSpecGenerator = new JpaSpecGenerator();

    Customer setupCustomer;
  
    @BeforeEach
    void setup(){
        setupCustomer = Customer.builder()
        .id(UUID.randomUUID())
        .name("Test Lte.")
        .contact("Telegram - @Test") 
        .shippingAddress("St. Jose 12")
        .build();
    }
}