package lumi.insert.app.controller.supply;
 
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
 
import com.github.f4b6a3.uuid.UuidCreator;

import tools.jackson.databind.ObjectMapper;

import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.mapper.AllSupplyMapper;
import lumi.insert.app.service.PdfService;
import lumi.insert.app.service.SupplyService;
import lumi.insert.app.service.XlsxService;

@SpringBootTest 
@WithMockUser(username = "admin", roles = {"ADMIN"})
@ActiveProfiles("test")
public abstract class BaseSupplyControllerTest {
     
    MockMvc mockMvc;

    @MockitoBean
    SupplyService supplyService;

    @Autowired
    AllSupplyMapper mapper;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PdfService pdfService;

    @MockitoBean
    XlsxService xlsxService;
  
    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }

    public SupplyResponse supplyResponse = new SupplyResponse(UuidCreator.getTimeOrderedEpochFast(), "INV", UuidCreator.getTimeOrderedEpochFast(), null, null, null, null, null, null, null, null, null, null, null, null, null, null);
}
