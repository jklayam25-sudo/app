package lumi.insert.app.controller.customer;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
 
import lumi.insert.app.dto.response.CustomerDetailResponse;
import lumi.insert.app.dto.response.CustomerNameResponse;
import lumi.insert.app.dto.response.CustomerResponse;
import lumi.insert.app.service.CustomerService;

@SpringBootTest
@WithMockUser(username = "admin", roles = {"ADMIN"})  
public abstract class BaseCustomerControllerTest {

    MockMvc mockMvc;

    @MockitoBean
    CustomerService customerService;

    CustomerDetailResponse customerDetailResponse = new CustomerDetailResponse(UUID.randomUUID(), "Test LTE.", "test@gmail.com", "Test - 00xxx", "St. Test 12 A", null, null, null, null);

    Slice<CustomerResponse> sliceCustomerResponse = new SliceImpl<>(List.of(new CustomerResponse(customerDetailResponse.id(), customerDetailResponse.name(), customerDetailResponse.email(), customerDetailResponse.contact())));

    Slice<CustomerNameResponse> sliceNames = new SliceImpl<>(List.of(new CustomerNameResponse(customerDetailResponse.id(), customerDetailResponse.name())));

    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }
}
