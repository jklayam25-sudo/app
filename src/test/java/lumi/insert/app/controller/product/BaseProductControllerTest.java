package lumi.insert.app.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import lumi.insert.app.service.ProductService;
import lumi.insert.app.utils.mapper.ProductMapperImpl;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @Autowired
    ProductMapperImpl productMapper;
}
