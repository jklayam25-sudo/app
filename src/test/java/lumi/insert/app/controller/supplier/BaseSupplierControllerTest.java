package lumi.insert.app.controller.supplier;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
 
import lumi.insert.app.dto.response.SupplierDetailResponse;
import lumi.insert.app.dto.response.SupplierNameResponse; 
import lumi.insert.app.service.SupplierService;

@SpringBootTest
@WithMockUser(username = "admin", roles = {"ADMIN"})  
@ActiveProfiles("test")
public abstract class BaseSupplierControllerTest {

    MockMvc mockMvc;

    @MockitoBean
    SupplierService supplierService;

    SupplierDetailResponse supplierDetailResponse = new SupplierDetailResponse(UUID.randomUUID(), "Test LTE.", "test@gmail.com", "Test - 00xxx", null, null, null, null);

    Slice<SupplierDetailResponse> sliceSupplierResponse = new SliceImpl<>(List.of(supplierDetailResponse));

    Slice<SupplierNameResponse> sliceNames = new SliceImpl<>(List.of(new SupplierNameResponse(supplierDetailResponse.id(), supplierDetailResponse.name())));

    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }
}
