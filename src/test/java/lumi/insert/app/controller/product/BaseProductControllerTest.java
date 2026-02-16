package lumi.insert.app.controller.product;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lumi.insert.app.service.ProductService;
import lumi.insert.app.utils.mapper.ProductMapperImpl;

@SpringBootTest 
@WithMockUser(username = "admin", roles = {"ADMIN"})
public abstract class BaseProductControllerTest { 
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @Autowired
    ProductMapperImpl productMapper;

    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }
}
